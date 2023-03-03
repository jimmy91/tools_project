# 结构型模式之---适配器模式

介绍
意图：将一个类的接口转换成客户希望的另外一个接口。适配器模式使得原本由于接口不兼容而不能一起工作的那些类可以一起工作。

主要解决：主要解决在软件系统中，常常要将一些"现存的对象"放到新的环境中，而新环境要求的接口是现对象不能满足的。

何时使用： 1、系统需要使用现有的类，而此类的接口不符合系统的需要。 2、想要建立一个可以重复使用的类，用于与一些彼此之间没有太大关联的一些类，包括一些可能在将来引进的类一起工作，这些源类不一定有一致的接口。 3、通过接口转换，将一个类插入另一个类系中。（比如老虎和飞禽，现在多了一个飞虎，在不增加实体的需求下，增加一个适配器，在里面包容一个虎对象，实现飞的接口。）

如何解决：继承或依赖（推荐）。

关键代码：适配器继承或依赖已有的对象，实现想要的目标接口。

应用实例： 1、美国电器 110V，中国 220V，就要有一个适配器将 110V 转化为 220V。 2、JAVA JDK 1.1 提供了 Enumeration 接口，而在 1.2 中提供了 Iterator 接口，想要使用 1.2 的 JDK，则要将以前系统的 Enumeration 接口转化为 Iterator 接口，这时就需要适配器模式。 3、在 LINUX 上运行 WINDOWS 程序。 4、JAVA 中的 jdbc。

优点： 1、可以让任何两个没有关联的类一起运行。 2、提高了类的复用。 3、增加了类的透明度。 4、灵活性好。

缺点： 1、过多地使用适配器，会让系统非常零乱，不易整体进行把握。比如，明明看到调用的是 A 接口，其实内部被适配成了 B 接口的实现，一个系统如果太多出现这种情况，无异于一场灾难。因此如果不是很有必要，可以不使用适配器，而是直接对系统进行重构。 2.由于 JAVA 至多继承一个类，所以至多只能适配一个适配者类，而且目标类必须是抽象类。

使用场景：有动机地修改一个正常运行的系统的接口，这时应该考虑使用适配器模式。

注意事项：适配器不是在详细设计时添加的，而是解决正在服役的项目的问题。

## 案例：
- 要让只能播放mp3的mediaPlayer能播mp4和vlc；
- 需要有一个适配器适配mp3播放器和mp4/vlc播放器；
- 通过mp3播放器播放mp4/vlc时，由适配器调用mp4/vlc播放器播放；但是调用mp3播放器的client是不清楚这个过程的
## 案例中角色：
- Target: 客户访问的目标：mp3播放器实现的接口；
- Adapater：适配者：适配器
- Adapatee：被适配者：mp4/vlc播放器

一句话理解：原有接口A和B，现要求通过A能访问到B，就需要一个中间层C适配(C实现A并持有B,调用B的方法)

## 3种模式：
1. 类适配：创建新类，继承源类，并实现新接口，例如 class adapter extends oldClass implements newFunc{}
2. 对象适配：创建新类持源类的实例，并实现新接口，例如 class adapter implements newFunc { private oldClass oldInstance ;}
3. 接口适配：创建新的抽象类实现旧接口方法。例如 abstract class adapter implements oldClassFunc { void newFunc();}