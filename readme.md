1、安装zeroc 在osx上直接使用brew安装 brew install ice

2、新建maven工程 ice，引入相关ice的jar
```xml
        <dependency>
            <groupId>com.zeroc</groupId>
            <artifactId>ice</artifactId>
            <version>3.6.0</version>
        </dependency>
        <dependency>
            <groupId>com.zeroc</groupId>
            <artifactId>freeze</artifactId>
            <version>3.6.0</version>
        </dependency>
        <dependency>
            <groupId>com.zeroc</groupId>
            <artifactId>glacier2</artifactId>
            <version>3.6.0</version>
        </dependency>
        <dependency>
            <groupId>com.zeroc</groupId>
            <artifactId>icebox</artifactId>
            <version>3.6.0</version>
        </dependency>
        <dependency>
            <groupId>com.zeroc</groupId>
            <artifactId>icediscovery</artifactId>
            <version>3.6.0</version>
        </dependency>
        <dependency>
            <groupId>com.zeroc</groupId>
            <artifactId>icegrid</artifactId>
            <version>3.6.0</version>
        </dependency>
        <dependency>
            <groupId>com.zeroc</groupId>
            <artifactId>icelocatordiscovery</artifactId>
            <version>3.6.0</version>
        </dependency>
        <dependency>
            <groupId>com.zeroc</groupId>
            <artifactId>icepatch2</artifactId>
            <version>3.6.0</version>
        </dependency>
        <dependency>
            <groupId>com.zeroc</groupId>
            <artifactId>icestorm</artifactId>
            <version>3.6.0</version>
        </dependency>

```

3、新建ice文件

```

module demo {
    interface Printer {
        void printString(string s);
    };
};

```

4、slice2java Printer.ice

5、查看src下文件目录为下：

java

│   │   ├── demo

│   │   │   ├── Callback_Printer_printString.java

│   │   │   ├── Printer.java

│   │   │   ├── PrinterHolder.java

│   │   │   ├── PrinterPrx.java

│   │   │   ├── PrinterPrxHelper.java

│   │   │   ├── PrinterPrxHolder.java

│   │   │   ├── _PrinterDisp.java

│   │   │   ├── _PrinterOperations.java

│   │   │   ├── _PrinterOperationsNC.java

│   │   │   ├── client

│   │   │   │   └── Client.java

│   │   │   ├── servant

│   │   │   │   └── PrinterI.java

│   │   │   └── server

│   │   │       └── Server.java

│   │   └── printer.ice

6、编写PrintI.java

```java

package demo.servant;

import Ice.Current;
import demo._PrinterDisp;

public class PrinterI extends _PrinterDisp{
    public void printString(String s, Current __current) {
        System.out.println(s);
    }
}

```

7、编写server

```java

package demo.server;

import demo.servant.PrinterI;

public class Server {
    public static void main(String[] args) {
        int status = 0;
        Ice.Communicator ic = null;
        try {
            //初使化连接，args可以传一些初使化参数，如连接超时时间，初使化客户连接池的数量等
            ic = Ice.Util.initialize(args);
            //创建名为SimplePrinterAdapter的适配器，并要求适配器使 用缺省的协议(TCP/IP侦听端口为10000的请求)
            Ice.ObjectAdapter adapter = ic.createObjectAdapterWithEndpoints("SimplePrinterAdapter", "default -p 10000");
            //实例化一个PrinterI对象，为Printer接口创建一个服务对象
            Ice.Object object = new PrinterI();
            //将服务单元增加到适配器中，并给服务对象指定名称为 SimplePrinter，该名称用于唯一确定一个服务单元
            adapter.add(object, Ice.Util.stringToIdentity("SimplePrinter"));
            //激活适配器，这样做的好处是可以等到所有资源就位后再触发
            adapter.activate();
            //让服务在退出之前，一直持续对请求的监听
            ic.waitForShutdown();
        } catch (Ice.LocalException e) {
            e.printStackTrace();
            status = 1;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            status = 1;
        } finally {
            if (ic != null) ic.destroy();
        }
        System.exit(status);
    }
}

```

8、编写client

```java

package demo.client;

import demo.PrinterPrx;
import demo.PrinterPrxHelper;

public class Client {
    public static void main(String[] args) {
        int status = 0;
        Ice.Communicator ic = null;
        try {
            // 初使化通信器
            ic = Ice.Util.initialize(args);
            // 传入远程服务单元的名称、网络协议、IP及端口，获取Printer 的远程代理，这里使用的stringToProxy方式
            Ice.ObjectPrx base = ic.stringToProxy("SimplePrinter:default -p 10000");
            //通过checkedCast向下转换，获取Printer接口的远程，并同 时检测根据传入的名称获取的服务单元是否Printer的代理接口，如果不是则返回null对象
            PrinterPrx printer = PrinterPrxHelper.checkedCast(base);
            if (printer == null) {
                throw new Error("Invalid proxy");
            }
            //把Hello World传给服务端，让服务端打印出来，因为这个方法 最终会在服务端上执行
            printer.printString("Hello World!");
        } catch (Ice.LocalException e) {
            e.printStackTrace();
            status = 1;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            status = 1;
        } finally {
            if (ic != null) {
                ic.destroy();
            }
        }
        System.exit(status);
    }
}

```

9、运行server，然后再运行client 在服务端可以看到Hello World！

