package com.example.android.doubanreading;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张俊秋 on 2017/3/7.
 */

public class ListFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<BookInfo> mBookInfos= new ArrayList<>();
    private TextView noDataText;
    private ImageView noDataImage;
    private TextView checkText;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_list,container,false);

        recyclerView= (RecyclerView) view.findViewById(R.id.book_list);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        noDataImage= (ImageView) view.findViewById(R.id.imageview_no_data);
        noDataText= (TextView) view.findViewById(R.id.textview_no_data);
        checkText= (TextView) view.findViewById(R.id.textview_check_internet);
        visibility(mBookInfos);
        return view;
    }

    private void visibility(List<BookInfo> bookInfos){
        if (bookInfos.size()==0){
            recyclerView.setVisibility(View.GONE);
            noDataImage.setVisibility(View.VISIBLE);
            noDataText.setVisibility(View.VISIBLE);
            checkText.setVisibility(View.VISIBLE);
        }else{
            recyclerView.setVisibility(View.VISIBLE);
            noDataImage.setVisibility(View.GONE);
            noDataText.setVisibility(View.GONE);
            checkText.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_menu,menu);
        MenuItem menuItem=menu.findItem(R.id.menu_search);
        SearchView searchView= (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                new FetchItemTask().execute(query);
                visibility(mBookInfos);
                setupAdapter();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void setupAdapter(){
        if (isAdded()){
            recyclerView.setAdapter(new MyAdapter(mBookInfos));
        }
    }

    private class MyviewHolder extends RecyclerView.ViewHolder{
        private TextView mTitle;
        private TextView mAuthor;
        private TextView mPubdate;
        private TextView mRate;
        public MyviewHolder(View itemView) {
            super(itemView);
            mTitle= (TextView) itemView.findViewById(R.id.textview_book_title);
            mAuthor= (TextView) itemView.findViewById(R.id.textview_book_author);
            mPubdate= (TextView) itemView.findViewById(R.id.textview_pubdate);
            mRate= (TextView) itemView.findViewById(R.id.textview_rate);

        }
        public void bindData(BookInfo bookInfo){
            mTitle.setText(bookInfo.getTitle());
            mAuthor.setText(bookInfo.getAuthors());
            mPubdate.setText(bookInfo.getPubdate());
            mRate.setText(bookInfo.getRating());
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<MyviewHolder>{
        List<BookInfo> mBookInfos;
        public MyAdapter(List<BookInfo> bookInfos){
            mBookInfos=bookInfos;
        }
        @Override
        public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(getActivity()).inflate(R.layout.item_list_view,parent,false);
            return new MyviewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyviewHolder holder, int position) {
            BookInfo bookInfo=mBookInfos.get(position);
            holder.bindData(bookInfo);
        }

        @Override
        public int getItemCount() {
            return mBookInfos.size();
        }
    }

    private class FetchItemTask extends AsyncTask<String,Void,List<BookInfo>>{

        @Override
        protected List<BookInfo> doInBackground(String... query) {
            return new DoubanBookFetchr().fetchItems(query[0]);
        }

        @Override
        protected void onPostExecute(List<BookInfo> bookInfos) {
            super.onPostExecute(bookInfos);
            mBookInfos=bookInfos;
            setupAdapter();
        }
    }
}
