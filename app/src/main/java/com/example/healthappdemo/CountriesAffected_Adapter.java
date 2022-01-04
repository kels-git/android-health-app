package com.example.healthappdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class CountriesAffected_Adapter extends ArrayAdapter<CountryModel> {

     Context mContext;
     List<CountryModel> mCountryModel_List;
    List<CountryModel> mCountryModel_FilteredList;

    public CountriesAffected_Adapter(Context context, List<CountryModel> CountryModel_List) {
        super(context, R.layout.list_custom_items, CountryModel_List);
        this.mContext = context;
        this.mCountryModel_List = CountryModel_List;
        this.mCountryModel_FilteredList = CountryModel_List;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_custom_items, null, true);
        TextView countryName = view.findViewById(R.id.TV_CountriesAffected_Display);
        ImageView countryFlag = view.findViewById(R.id.IMG_Country_Flag);

        countryName.setText(mCountryModel_FilteredList.get(position).getCountry());
        Glide.with(mContext).load(mCountryModel_FilteredList.get(position).getFlag()).into(countryFlag);

        return view;


    }

    @Override
    public int getCount() {
        return mCountryModel_FilteredList.size();
    }


    @Nullable
    @Override
    public CountryModel getItem(int position) {
        return mCountryModel_FilteredList.get(position);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraints) {

                FilterResults filterResults = new FilterResults();
                if(constraints == null || constraints.length() == 0){
                    filterResults.count = mCountryModel_List.size();
                    filterResults.values = mCountryModel_List;

                }else{

                    List<CountryModel> resultModel = new ArrayList<>();
                    String searchString = constraints.toString().toLowerCase();
                    for(CountryModel itemModel: mCountryModel_List){
                        if(itemModel.getCountry().toLowerCase().contains(searchString)){
                            resultModel.add(itemModel);
                        }
                        filterResults.count = resultModel.size();
                        filterResults.values = resultModel;
                    }

                }
               return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mCountryModel_FilteredList = (List<CountryModel>) filterResults.values;
                CountriesAffectedActivity.mCountryModel_List =  (List<CountryModel>) filterResults.values;
                notifyDataSetChanged();

            }
        };
        return filter;
    }
}
