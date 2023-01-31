package app.project.controller;

import app.netty.MessageDto;
import app.project.service.ImPushService;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author
 * @date 2020-01-13-20:15
 */
@Api(tags = "IM模块-消息发送")
@RestController
@RequestMapping("/push")
@Validated
public class ImPushController {

    @Autowired
    private ImPushService pushService;


    /**
     * 推送给所有
     *
     * @param msg
     */
    @ApiOperation(value = "推送给所有", notes="")
    @PostMapping("/sendMsgToAll")
    public void sendMsgToAll(@RequestParam("msg") String msg) {
        MessageDto messageDto = new MessageDto();
        String uuid = String.valueOf(UUID.randomUUID()).replace("-", "");
        messageDto.setNettyId(uuid);
        messageDto.setMsg(msg);
        messageDto.setStatus("0000");
        messageDto.setType("netty_msg");
        messageDto.setTypeId("netty_web_push");
        messageDto.setDate(DateUtil.formatDateTime(DateUtil.date()));
        Map<String, Object> map = new HashMap<>(16);
        map.put("id", uuid);
        map.put("name", "王五");
        map.put("number", "A-1257564246");
        messageDto.setObjectMap(map);
        pushService.sendMsgToAll(messageDto);
    }


    /**
     * 推送给指定
     *
     * @param name
     * @param msg
     */
    @ApiOperation(value = "推送给指定", notes="")
    @PostMapping("/sendMsgToOne")
    public void sendMsgToOne(@RequestParam("name") String name, @RequestParam("msg") String msg, @RequestParam("typeId") String typeId) {
        MessageDto messageDto = new MessageDto();
        String uuid = String.valueOf(UUID.randomUUID()).replace("-", "");
        messageDto.setNettyId(uuid);
        messageDto.setMsg(msg);
        messageDto.setStatus("0000");
        messageDto.setType("netty_msg");
        messageDto.setTypeId(typeId);
        messageDto.setDate(DateUtil.formatDateTime(DateUtil.date()));
        Map<String, Object> map = new HashMap<>(16);
        map.put("id", uuid);
        map.put("name", name);
        map.put("number", "1257564246");
        messageDto.setObjectMap(map);
        pushService.sendMsgToOne(messageDto.getType(), messageDto);
    }
}

