package demo.servant;

import Ice.Current;
import demo._PrinterDisp;

public class PrinterI extends _PrinterDisp{
    public void printString(String s, Current __current) {
        System.out.println(s);
    }
}
