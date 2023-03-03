package utils.design.factory;

/** 产品二
 * @author Jimmy
 * @date 2021/3/23 0023
 */
public class Square implements Shape {
	@Override
	public void draw() {
		System.out.println("Inside Square::draw() method.");
	}
}