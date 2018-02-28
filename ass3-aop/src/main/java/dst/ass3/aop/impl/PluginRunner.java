package dst.ass3.aop.impl;

import dst.ass3.aop.IPluginExecutable;

/**
 * Created by amra.
 */


public class PluginRunner implements Runnable {
    private IPluginExecutable iPluginExecutable;

    public PluginRunner(IPluginExecutable iPluginExecutable){
        this.iPluginExecutable=iPluginExecutable;
    }

    @Override
    public void run() {
        try {
            iPluginExecutable.execute();
        } catch (Exception e) {
            System.err.println("Execution of "+iPluginExecutable.getClass().getSimpleName()+" failed!");
            e.printStackTrace();
        }
    }
}
