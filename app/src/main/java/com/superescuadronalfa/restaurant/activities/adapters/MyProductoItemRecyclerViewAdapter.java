package com.superescuadronalfa.restaurant.activities.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.superescuadronalfa.restaurant.R;
import com.superescuadronalfa.restaurant.app.AppController;
import com.superescuadronalfa.restaurant.dbEntities.Producto;
import com.superescuadronalfa.restaurant.dbEntities.control.ControlProductos;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Producto} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class MyProductoItemRecyclerViewAdapter extends RecyclerView.Adapter<MyProductoItemRecyclerViewAdapter.ViewHolder> implements Filterable {

    private final List<Producto> mValues;
    private List<Producto> mValuesFiltered;
    private final OnListFragmentInteractionListener mListener;


    MyProductoItemRecyclerViewAdapter(List<Producto> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        mValuesFiltered = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_productoitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mValuesFiltered = mValues;
                } else {
                    List<Producto> filteredList = new ArrayList<>();
                    for (Producto row : mValues) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getNombreProducto().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    mValuesFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mValuesFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mValuesFiltered = (ArrayList<Producto>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValuesFiltered.get(position);
        if (!mValuesFiltered.get(position).hasImageLoaded()) {
            ControlProductos.getInstance().burcarImagen(mValuesFiltered.get(position), AppController.getInstance(), holder.mImageView);
        } else {
            holder.mImageView.setImageBitmap(mValuesFiltered.get(position).getImage());
        }
        holder.mContentView.setText(mValuesFiltered.get(position).getNombreProducto());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValuesFiltered.size();
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Producto item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final ImageView mImageView;
        final TextView mContentView;
        public Producto mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = view.findViewById(R.id.producto_photo);
            mContentView = view.findViewById(R.id.producto_name);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
