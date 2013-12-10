package net.carlosjg.gotodo;

import java.util.List;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

    public class DataListLoaderProject extends AsyncTaskLoader<List<Project>> {
         
        List<Project> mProjects;
        String Estado;
 
        public DataListLoaderProject(Context context, String Estado) {
            super(context);
            this.Estado=Estado;
            
        }
 
        public List<Project> loadInBackground() {       	
            List<Project> Lista = Project.findWithQuery(Project.class, "Select * from Project order by PROJ_PR DESC"); 
            return Lista;
        }

        @Override public void deliverResult(List<Project> listOfData) {
            if (isReset()) {
                if (listOfData != null) {
                    onReleaseResources(listOfData);
                }
            }
            List<Project> oldApps = listOfData;
            mProjects = listOfData;
 
            if (isStarted()) {
                super.deliverResult(listOfData);
            }
 
            if (oldApps != null) {
                onReleaseResources(oldApps);
            }
        }
 
        @Override protected void onStartLoading() {
            if (mProjects != null) {
                // Si hay datos se envian
                deliverResult(mProjects);
            }
 
 
            if (takeContentChanged() || mProjects == null) {
                forceLoad();
            }
        }
 
        @Override protected void onStopLoading() {
            cancelLoad();
        }
 
        @Override public void onCanceled(List<Project> apps) {
            super.onCanceled(apps);
 
            onReleaseResources(apps);
        }
 
        @Override protected void onReset() {
            super.onReset();
 
            onStopLoading();

            if (mProjects != null) {
                onReleaseResources(mProjects);
                mProjects = null;
            }
        }
 
        protected void onReleaseResources(List<Project> apps) {}
         
    }