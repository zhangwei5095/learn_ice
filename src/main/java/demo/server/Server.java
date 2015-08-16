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
