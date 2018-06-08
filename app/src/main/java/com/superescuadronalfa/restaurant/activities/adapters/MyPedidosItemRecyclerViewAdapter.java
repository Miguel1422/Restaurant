package com.superescuadronalfa.restaurant.activities.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.superescuadronalfa.restaurant.R;
import com.superescuadronalfa.restaurant.dbEntities.OrdenProducto;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link OrdenProducto} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class MyPedidosItemRecyclerViewAdapter extends RecyclerView.Adapter<MyPedidosItemRecyclerViewAdapter.ViewHolder> {

    private final List<OrdenProducto> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final Context context;

    public MyPedidosItemRecyclerViewAdapter(Context context, List<OrdenProducto> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_pedidositem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.mItem = mValues.get(position);
        // holder.mImageView.setImageBitmap(mValues.get(position).getImage());
        String content = mValues.get(position).getTipoProducto().getProducto().getNombreProducto() + " "
                + mValues.get(position).getTipoProducto().getNombreTipo();
        holder.mContentView.setText(content);
        holder.mDescriptionView.setText(mValues.get(position).getComentarios());
        holder.mCantidadView.setText(("Cnt: " + mValues.get(position).getCantidad()));

        switch (mValues.get(position).getStatus()) {
            case "En cola":
                holder.mImageView.setImageResource(R.drawable.en_espera);
                break;
            case "Finalizado":
                holder.mImageView.setImageResource(R.drawable.finalizado);
                break;
            case "Cocinando":
                holder.mImageView.setImageResource(R.drawable.cocinando);
                break;
        }

        OrdenProducto ac = mValues.get(position);

        if (ac.getVariantesDeLaOrden().size() > 0) {
            // TODO agregar la lista de variantes
            // Hecho en activity editar, Aqui es necesario?
            Log.w("Nada", "Aqui se deberian mostar las mesa");
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
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onImageClicked(holder.mItem, position);
                }
            }
        });
        holder.mBtnRemoveView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onRemoveClicked(holder.mItem, position);
            }
        });

        holder.mBtnEditView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onEditClicked(holder.mItem, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void deleteItem(int position) {
        mValues.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mValues.size());
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(OrdenProducto item);

        void onRemoveClicked(OrdenProducto item, int index);

        void onEditClicked(OrdenProducto item, int index);

        void onImageClicked(OrdenProducto item, int index);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final ImageView mImageView;
        final TextView mContentView;
        final Button mBtnRemoveView;
        final Button mBtnEditView;
        final TextView mCantidadView;
        private final TextView mDescriptionView;

        public OrdenProducto mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = view.findViewById(R.id.producto_photo);
            mContentView = view.findViewById(R.id.producto_name);
            mDescriptionView = view.findViewById(R.id.descripcion_pedido);
            mBtnRemoveView = view.findViewById(R.id.button_delete);
            mCantidadView = view.findViewById(R.id.producto_cantidad);
            mBtnEditView = view.findViewById(R.id.button_edit);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
