package net.carlosjg.gotodo;

import java.util.List;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

    public class DataListLoaderNote extends AsyncTaskLoader<List<Note>> {
         
        List<Note> mNotes;
        String Estado;
        String Project;
 
        public DataListLoaderNote(Context context, String Estado, String Project) {
            super(context);
            this.Estado=Estado;
            this.Project=Project;            
        }
 
        public List<Note> loadInBackground() {
        	List<Note> Lista;
        	if (Project.equals("null")){
        		Lista = Note.find(Note.class, "ESTADO = ?", new String[] {Estado},null,"PRIORIDAD DESC",null);        		
        	}
        	else
        	{
        		Lista = Note.find(Note.class, "ESTADO = ? and PROJECT_ID = ?", new String[] {Estado, Project},null,"PRIORIDAD DESC",null);
        		
        	}
            return Lista;
        }
         
        @Override public void deliverResult(List<Note> listOfData) {
            if (isReset()) {
                if (listOfData != null) {
                    onReleaseResources(listOfData);
                }
            }
            List<Note> oldApps = listOfData;
            mNotes = listOfData;
 
            if (isStarted()) {
                super.deliverResult(listOfData);
            }
 
            if (oldApps != null) {
                onReleaseResources(oldApps);
            }
        }
 
        @Override protected void onStartLoading() {
            if (mNotes != null) {
                deliverResult(mNotes);
            }
 
 
            if (takeContentChanged() || mNotes == null) {
                forceLoad();
            }
        }
 
        @Override protected void onStopLoading() {
            cancelLoad();
        }
 
        @Override public void onCanceled(List<Note> apps) {
            super.onCanceled(apps);
            onReleaseResources(apps);
        }
 

        @Override protected void onReset() {
            super.onReset();
            onStopLoading();

            if (mNotes != null) {
                onReleaseResources(mNotes);
                mNotes = null;
            }
        }
 
        protected void onReleaseResources(List<Note> apps) {}
         
    }