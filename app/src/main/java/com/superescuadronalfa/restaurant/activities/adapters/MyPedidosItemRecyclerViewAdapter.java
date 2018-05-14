package com.superescuadronalfa.restaurant.activities.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.superescuadronalfa.restaurant.R;
import com.superescuadronalfa.restaurant.dbEntities.OrdenProducto;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link OrdenProducto} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        // holder.mImageView.setImageBitmap(mValues.get(position).getImage());
        String content = mValues.get(position).getTipoProducto().getProducto().getNombreProducto() + " "
                + mValues.get(position).getTipoProducto().getNombreTipo();
        holder.mContentView.setText(content);
        holder.mDescriptionView.setText(mValues.get(position).getComentarios());
        holder.mCantidadView.setText(("Cnt: " + mValues.get(position).getCantidad()));

        OrdenProducto ac = mValues.get(position);

        if (ac.getVariantesDeLaOrden().size() > 0) {
            TextView textView = new TextView(context);
            textView.setText("Hello. I'm a header view");
            holder.mListView.addHeaderView(textView);
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

        holder.mBtnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onRemoveClicked(holder.mItem, position);
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
        // TODO: Update argument type and name
        void onListFragmentInteraction(OrdenProducto item);

        void onRemoveClicked(OrdenProducto item, int index);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImageView;
        public final TextView mContentView;
        public final TextView mDescriptionView;
        public final Button mBtnView;
        public final TextView mCantidadView;
        public final ListView mListView;

        public OrdenProducto mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = view.findViewById(R.id.producto_photo);
            mContentView = view.findViewById(R.id.producto_name);
            mDescriptionView = view.findViewById(R.id.descripcion_pedido);
            mBtnView = view.findViewById(R.id.button_delete);
            mCantidadView = view.findViewById(R.id.producto_cantidad);
            mListView = view.findViewById(R.id.lv_pedidos);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
