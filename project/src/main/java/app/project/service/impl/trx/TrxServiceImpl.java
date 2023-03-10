package app.project.service.impl.trx;

import app.project.entity.trx.TrxDeptEntity;
import app.project.entity.trx.TrxEmpEntity;
import app.project.mapper.trx.TrxDeptMapper;
import app.project.mapper.trx.TrxEmpMapper;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import utils.tools.mybatis.LambdaQueryWrapperX;
import utils.tools.random.RandomUtil;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author Jimmy
 * https://www.zhihu.com/question/506754250/answer/2645488294?utm_id=0
 */
@Slf4j
@Service
public class TrxServiceImpl extends ServiceImpl<TrxEmpMapper, TrxEmpEntity> {

    @Resource
    private TrxDeptMapper deptMapper;

    @Resource
    private TrxEmpMapper empMapper;

    @Autowired
    private DataSourceTransactionManager txManager;

    @Autowired
    private TrxAService aService;

    @Autowired
    private TrxBService bService;

    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1,5,0, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<>(5));


    public TrxEmpEntity getEmp(Integer no) {
        return empMapper.selectEmpByNo(no);
    }

    public String noTrx() {
        String deptNo = randomDeptNo();
        TrxDeptEntity dept = provideDept(deptNo);
        List<TrxEmpEntity> emps = provideEmps(deptNo, 5);

        aService.insertDept(dept, emps);
        return deptNo;
    }

    public String trx01() {
        String deptNo = randomDeptNo();
        TrxDeptEntity dept = provideDept(deptNo);
        List<TrxEmpEntity> emps = provideEmps(deptNo, 5);

        aService.insertDeptTransaction(dept, emps);
        return deptNo;
    }

    public String trx02() {
        String deptNo = randomDeptNo();
        TrxDeptEntity dept = provideDept(deptNo);
        List<TrxEmpEntity> emps = provideEmps(deptNo, 5);

        aService.insertSteps(dept, emps);
        return deptNo;
    }

    public String trx03() {
        String deptNo = randomDeptNo();
        TrxDeptEntity dept = provideDept(deptNo);
        List<TrxEmpEntity> emps = provideEmps(deptNo, 5);

        aService.transactionParallel(dept, emps);
        return deptNo;
    }

    public String trx04() throws ExecutionException, InterruptedException {
        String deptNo = randomDeptNo();
        TrxDeptEntity dept = provideDept(deptNo);
        List<TrxEmpEntity> emps = provideEmps(deptNo, 10);

        aService.transactionAsyncInner(dept, emps);
        return deptNo;
    }

    @Transactional
    public String trx05() throws InterruptedException {
        TrxEmpEntity emp = new TrxEmpEntity();
        Integer empno = 10000 + cn.hutool.core.util.RandomUtil.randomInt(10000);
        emp.setEmpno(empno);
        emp.setEname("??????trx05");
        empMapper.insert(emp);

        Thread thread = new Thread() {
            @Override
            public void run() {
                TrxEmpEntity one = empMapper.selectById(empno);
                log.info("?????????????????????????????????: >>>>>> " + one);
            }
        };
        thread.start();
        TrxEmpEntity one = empMapper.selectById(empno);
        log.info("?????????????????????????????????: >>>>>> " + one);
        // ????????????????????????
        thread.join();

        // ??????????????????????????????
        throw new IllegalArgumentException("??? bug ???");
    }

    @Transactional
    public String trx06() throws InterruptedException {
        TrxEmpEntity emp = new TrxEmpEntity();
        Integer empno = 10000 + cn.hutool.core.util.RandomUtil.randomInt(10000);
        emp.setEmpno(empno);
        emp.setEname("??????trx06");
        empMapper.insert(emp);

        final Thread[] thread = {null};

        // ??????????????????????????????
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                thread[0] = new Thread() {
                    @Override
                    public void run() {
                        // ????????????????????????????????????????????????????????????
                        TrxEmpEntity one = empMapper.selectById(empno);
                        log.info("???????????????:" + one);
                    }
                };
                thread[0].start();

                // ????????????????????????
                try {
                    thread[0].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // ??????????????????????????????
                throw new IllegalArgumentException("??? bug ???");
            }
        });
        return empno.toString();
    }

    public String trx07() {
        String deptNo = randomDeptNo();
        TrxDeptEntity dept = provideDept(deptNo);
        TransactionDefinition transactionDefinition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = txManager.getTransaction(transactionDefinition);
        try {
            deptMapper.insert(dept);
            int i = 1 / 0;
            txManager.commit(status);
        } catch (Exception e) {
            log.error("??????????????????");
            txManager.rollback(status);
        }
        return deptNo;
    }

    public String trx08() {
        String deptNo = randomDeptNo();
        TrxDeptEntity dept = provideDept(deptNo);
        List<TrxEmpEntity> emps = provideEmps(deptNo, 5);
        // ??????????????????????????????????????????????????????trx03()??????
        transactionParallel(dept, emps);
        return deptNo;
    }

    @Transactional(rollbackFor = Exception.class)
    public void transactionParallel(TrxDeptEntity dept, List<TrxEmpEntity> emps) {
        deptMapper.insert(dept);
        bService.insertTrxEmpEntitysTransactionRequired(emps);
        if (true) throw new IllegalArgumentException("??? bug ???");
    }

    @Transactional(rollbackFor = Exception.class)
    public String trx09() {
        String deptNo = randomDeptNo();
        TrxDeptEntity dept = provideDept(deptNo);
        List<TrxEmpEntity> emps = provideEmps(deptNo, 5);
        aService.trx09(dept, emps);
        return deptNo;
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public String trx10() throws ExecutionException, InterruptedException, TimeoutException {
        String deptNo = randomDeptNo();
        List<TrxEmpEntity> empEntities = Collections.emptyList();
        // ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        empEntities = empMapper.selectList(new LambdaQueryWrapperX<TrxEmpEntity>()
              .between(TrxEmpEntity::getEmpno, 50600, 59031));
       // log.info(">>>>>> ?????????????????? count={}", empEntities.size());
        final Integer updateEmpNo = 7104;
        // ?????????????????????
        final Integer innerEmpNo = 51658;
        // ????????????????????????????????????
        Callable<TrxEmpEntity> callable = () -> {
            // ????????????????????????????????????
            TrxEmpEntity emp = new TrxEmpEntity();
            emp.setDeptno(deptNo);
            emp.setEname(RandomUtil.username());
            emp.setEmpno(innerEmpNo);
            empMapper.insert(emp);
            // ??????????????????
            String rName = RandomUtil.username();
            TrxEmpEntity updateEmp = new TrxEmpEntity();
            updateEmp.setEmpno(updateEmpNo);
            updateEmp.setEname(rName);
            empMapper.updateById(updateEmp);

            List<TrxEmpEntity> newEmps = empMapper.selectList(new LambdaQueryWrapperX<TrxEmpEntity>()
                    .between(TrxEmpEntity::getEmpno, 50600, 59031));
            log.info(">>>>>> ??????????????????????????????????????? count={}, ????????????????????? rName={}", newEmps.size(), rName);
            return emp;
        };
        Future<TrxEmpEntity> future = threadPoolExecutor.submit(callable);
        TrxEmpEntity addEmp = future.get(5, TimeUnit.SECONDS);
        log.info(">>>>>> ???????????? {}", addEmp);
        ThreadUtil.sleep(100);

        String rName = empMapper.selectEmpByNo(updateEmpNo).getEname();
        TrxEmpEntity insertInnerEntity = empMapper.selectEmpByNo(innerEmpNo);
        empEntities = empMapper.selectList(new LambdaQueryWrapperX<TrxEmpEntity>()
                .between(TrxEmpEntity::getEmpno, 50600, 59031));
        List<TrxEmpEntity> empEntitiesV2 = empMapper.selectList(new LambdaQueryWrapperX<TrxEmpEntity>()
                .between(TrxEmpEntity::getEmpno, 50600 - 5, 59031 + 5));
        log.info(">>>>>> (???????????????????????????)????????????????????????????????????????????????????????????????????????????????????????????? rName = {}", rName);
        log.info(">>>>>> (???????????????????????????)????????????ID???????????????????????????????????????????????????????????????insertInnerEntity = {}", insertInnerEntity);
        log.info(">>>>>> (???????????????????????????)?????????????????????????????????????????????????????????????????????????????????????????????????????????(count?????????countV2)" +
                "??????????????????????????????????????????????????? count=countV2??? count = {} countV2 = {} ", empEntities.size(), empEntitiesV2.size());

        // ?????????????????????
        TrxEmpEntity emp = new TrxEmpEntity();
        emp.setDeptno(deptNo);
        emp.setEname(RandomUtil.username());
        emp.setEmpno(innerEmpNo+1);
        empMapper.insert(emp);

        empEntities = empMapper.selectList(new LambdaQueryWrapperX<TrxEmpEntity>()
                .between(TrxEmpEntity::getEmpno, 50600, 59031));
        log.info(">>>>>> ??????????????????insert??????(???????????????????????????)????????????????????????????????????????????????????????????????????????????????????????????????????????? count={} ", empEntities.size());
        return deptNo;
    }


    private List<TrxEmpEntity> provideEmps(String deptNo, int count) {
        List<TrxEmpEntity> emps = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            TrxEmpEntity emp = new TrxEmpEntity();
            emp.setDeptno(deptNo);
            emp.setEname(RandomUtil.username());
            emp.setEmpno(RandomUtils.nextInt(10, 100000));
            emp.setJob(RandomUtil.job());
            emp.setHiredate(RandomUtil.date());
            emp.setMgr(RandomUtils.nextInt(10, 1000));
            emp.setSal(new BigDecimal(RandomUtils.nextDouble(1000, 100000)));
            emp.setComm(new BigDecimal(RandomUtils.nextDouble(0, 1000)));
            emps.add(emp);
        }
        return emps;
    }

    private TrxDeptEntity provideDept(String deptNo) {
        TrxDeptEntity dept = new TrxDeptEntity();
        dept.setDname(RandomUtil.username());
        dept.setLoc(RandomUtil.address());
        dept.setDeptno(deptNo);
        return dept;
    }

    @NotNull
    private static String randomDeptNo() {
        String deptNo = String.format("%s#%05d", DateUtil.today(), RandomUtil.randomNumber(10000));
        log.info("????????????deptNo >>>>>>> {}", deptNo);
        return deptNo;
    }


}
