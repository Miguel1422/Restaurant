package com.superescuadronalfa.restaurant.activities.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.superescuadronalfa.restaurant.R;
import com.superescuadronalfa.restaurant.dbEntities.ProductoVariante;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ProductoVariante} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class MyVairanteItemRecyclerViewAdapter extends RecyclerView.Adapter<MyVairanteItemRecyclerViewAdapter.ViewHolder> {

    private final List<ProductoVariante> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final HashSet<ProductoVariante> variantesSeleccionadas = new HashSet<>();

    public List<ProductoVariante> getSelectedItems() {
        return new ArrayList<>(variantesSeleccionadas);
    }


    public void setSelectedItems(List<ProductoVariante> variantes) {
        variantesSeleccionadas.addAll(variantes);
        // notifyItemRangeChanged(position, mValues.size());
        notifyDataSetChanged();

    }

    public MyVairanteItemRecyclerViewAdapter(List<ProductoVariante> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_vairanteitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mcheckBoxView.setText(mValues.get(position).getNombreVariante());
        holder.mcheckBoxView.setChecked(variantesSeleccionadas.contains(mValues.get(position)));
        holder.mcheckBoxView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    variantesSeleccionadas.add(mValues.get(position));
                } else {
                    variantesSeleccionadas.remove(mValues.get(position));
                }

            }
        });
    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final CheckBox mcheckBoxView;

        public ProductoVariante mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mcheckBoxView = view.findViewById(R.id.checkBox);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mcheckBoxView.getText() + "'";
        }
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(ProductoVariante item);
    }
}
