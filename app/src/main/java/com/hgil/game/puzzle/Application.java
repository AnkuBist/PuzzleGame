package com.hgil.game.puzzle;

import java.util.IdentityHashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * This project's application context. Allocates and maintains
 * resources used throughout the project's activities.
 */
public class Application extends android.app.Application
{
 public Future<?> submitBackgroundTask(Runnable task)
 {
  return getBackgroundThreads().submit(task);
 }

 public <T> Future<T> submitBackgroundTask(Callable<T> task)
 {
  return getBackgroundThreads().submit(task);
 }
 
 public void onActivityCreate(Activity activity)
 {
  activities.put(activity, null);
 }

 public void onActivityDestroy(Activity activity)
 {
  activities.remove(activity);
  if (activities.isEmpty())
   onTerminate();
 }

 /**
  * This method is called when all application's
  * activities have been {@link #onActivityDestroy(Activity) destroyed}. 
  */
 @Override
 public void onTerminate()
 {
  if (null != backgroundThreads)
  {
   backgroundThreads.shutdown();
   backgroundThreads = null;
  }
  super.onTerminate();
 }

 protected ExecutorService getBackgroundThreads()
 {
  if (null == backgroundThreads)
   backgroundThreads = Executors.newCachedThreadPool();
  return backgroundThreads;
 }

 private IdentityHashMap<Activity, Object> activities = new IdentityHashMap<Activity, Object>();
 private ExecutorService backgroundThreads;
}
