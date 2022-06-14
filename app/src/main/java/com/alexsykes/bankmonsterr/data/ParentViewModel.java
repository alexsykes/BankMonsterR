package com.alexsykes.bankmonsterr.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ParentViewModel extends AndroidViewModel {
    private ParentRepository parentRepository;

    private final LiveData<List<Parent>> allParents;

    public ParentViewModel(@NonNull Application application) {
        super(application);
        parentRepository = new ParentRepository(application);
        allParents = parentRepository.getAllParents();
    }

    public LiveData<List<Parent>> getAllParents() {
        return allParents;
    }

    public void insert(Parent parent) {
        parentRepository.insert(parent);
    }
}
