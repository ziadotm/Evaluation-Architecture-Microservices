package com.example.service;


import com.example.beans.Mariage;
import com.example.dao.GenericDao;

public class MariageService extends GenericDao<Mariage> {
    public MariageService() { super(Mariage.class); }
}

