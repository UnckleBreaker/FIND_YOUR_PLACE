package com.example.helloar;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class City_places extends Fragment {
    private RecyclerView recyclerView ;
    private WidgetAdapter mAdapter;
    private SQLiteDatabase database;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.myfragment, container, false);
        Dhelper dbHelper = new Dhelper(getContext());
        database = dbHelper.getReadableDatabase();
        recyclerView=view.findViewById(R.id.recycleview);
        mAdapter = new WidgetAdapter(getContext(),getAllItems());
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mAdapter.swapCursor(getAllItems());
        recyclerView.setAdapter(mAdapter);


        return view;
    }    private Cursor getAllItems() {
        return database.query(
                DBContract.PaymentEntry.TABLE,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }



}
