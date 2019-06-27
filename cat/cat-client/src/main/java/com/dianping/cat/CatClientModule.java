package com.dianping.cat;

import org.unidal.helper.Threads;
import org.unidal.initialization.AbstractModule;
import org.unidal.initialization.DefaultModuleContext;
import org.unidal.initialization.Module;
import org.unidal.initialization.ModuleContext;
import org.unidal.lookup.annotation.Named;

import java.util.concurrent.ExecutorService;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/3/28
 */
@Named(type = Module.class, value = CatClientModule.ID)
public class CatClientModule extends AbstractModule {

    public static final String ID = "cat-client";

    @Override
    protected void execute(ModuleContext ctx) throws Exception {
        ctx.info("Current working directory is " + System.getProperty("user.dir"));

        Threads.addListener(new CatThreadListener(ctx));

//        Cat.getInstance().setContainer(((DefaultModuleContext) ctx).getContainer());
    }

    @Override
    public Module[] getDependencies(ModuleContext moduleContext) {
        return null;
    }

    public static final class CatThreadListener extends Threads.AbstractThreadListener {
        private final ModuleContext m_ctx;

        private CatThreadListener(ModuleContext ctx) {
            m_ctx = ctx;
        }

        @Override
        public void onThreadGroupCreated(ThreadGroup group, String name) {
            m_ctx.info(String.format("Thread group(%s) created.", name));
        }

        @Override
        public void onThreadPoolCreated(ExecutorService pool, String name) {
            m_ctx.info(String.format("Thread pool(%s) created.", name));
        }

        @Override
        public void onThreadStarting(Thread thread, String name) {
            m_ctx.info(String.format("Starting thread(%s) ...", name));
        }

        @Override
        public void onThreadStopping(Thread thread, String name) {
            m_ctx.info(String.format("Stopping thread(%s)", name));
        }

        @Override
        public boolean onUncaughtException(Thread thread, Throwable e) {
            m_ctx.error(String.format("Uncaught exception thrown out of thread(%s)", thread.getName()), e);
            return true;
        }
    }
}
