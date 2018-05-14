package com.superescuadronalfa.restaurant.activities.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.superescuadronalfa.restaurant.R;
import com.superescuadronalfa.restaurant.dbEntities.Mesa;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Mesa} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyMesaItemRecyclerViewAdapter extends RecyclerView.Adapter<MyMesaItemRecyclerViewAdapter.ViewHolder> {

    private final List<Mesa> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyMesaItemRecyclerViewAdapter(List<Mesa> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_mesaitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getNombre());
        holder.mContentView.setText(mValues.get(position).getEstadoMesa().getNombre());
        if (mValues.get(position).getEstadoMesa().getNombre().equals("Disponible")) {
            holder.imageView.setImageResource(R.drawable.restaurant_empty_table);
        } else {
            holder.imageView.setImageResource(R.drawable.restaurant_table);
        }


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

        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListButtonClicked(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Mesa item);

        void onListImageClicked(Mesa mItem);

        void onListButtonClicked(Mesa mItem);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final Button btn;
        public final ImageView imageView;
        public Mesa mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = view.findViewById(R.id.producto_name);
            mContentView = view.findViewById(R.id.person_age);
            imageView = view.findViewById(R.id.producto_photo);
            btn = view.findViewById(R.id.button_delete);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

}
