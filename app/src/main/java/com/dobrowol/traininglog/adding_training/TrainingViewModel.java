package com.dobrowol.traininglog.adding_training;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.Date;
import java.util.List;

public class TrainingViewModel extends AndroidViewModel {

    private TrainingRepository mRepository;

    private LiveData<List<Training>> mAllTrainings;

    public TrainingViewModel(Application application) {
        super(application);
        mRepository = new TrainingRepository(application);
        mAllTrainings = mRepository.getAllExercises();
    }

    public LiveData<List<Training>> getAllTrainings() { return mAllTrainings; }

    public LiveData<List<Training>> getTrainingByDate(Date date){return mRepository.getTrainingFrom(date);}

    public void insert(Training training) { mRepository.insert(training); }

    public void update(Training training) { mRepository.update(training);

    }

    public void delete(Training training) {
        mRepository.delete(training);
    }

    public LiveData<Training> getTraining(String trainingId) {
        return mRepository.getTrainingById(trainingId);
    }

    public LiveData<List<Integer>> getAllGeneralLoads() {
        return mRepository.getAllGeneralLoads();
    }
}

