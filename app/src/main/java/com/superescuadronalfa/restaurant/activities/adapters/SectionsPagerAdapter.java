package com.superescuadronalfa.restaurant.activities.adapters;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.superescuadronalfa.restaurant.R;
import com.superescuadronalfa.restaurant.dbEntities.Producto;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

    private List<List<Producto>> productosPorCategoria;
    private static int columnCount;

    public SectionsPagerAdapter(FragmentManager fm, List<List<Producto>> productosPorCategoria) {
        this(fm, productosPorCategoria, 2);
    }

    public SectionsPagerAdapter(FragmentManager fm, List<List<Producto>> productosPorCategoria, int columnCount) {
        super(fm);
        this.productosPorCategoria = productosPorCategoria;
        SectionsPagerAdapter.columnCount = columnCount;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return PlaceholderFragment.newInstance(productosPorCategoria.get(position));
    }

    @Override
    public int getCount() {
        return productosPorCategoria.size();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private List<Producto> productos;
        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         *
         * @param productos Los productos pertenecientes a esta categoria
         */
        public static PlaceholderFragment newInstance(List<Producto> productos) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            fragment.setProductos(productos);
            Bundle args = new Bundle();

            args.putParcelableArrayList(ARG_SECTION_NUMBER, (ArrayList<? extends Parcelable>) productos);
            fragment.setArguments(args);
            return fragment;
        }

        private void setProductos(List<Producto> productos) {
            this.productos = productos;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_productos, container, false);


            RecyclerView rv = rootView.findViewById(R.id.rv);
            Context context = rv.getContext();
            RecyclerView recyclerView = rv;
            recyclerView.setLayoutManager(new GridLayoutManager(context, Math.max(columnCount, 2)));

            ArrayList<Producto> productos = getArguments().getParcelableArrayList(ARG_SECTION_NUMBER);
            recyclerView.setAdapter(new MyProductoItemRecyclerViewAdapter(productos, new MyProductoItemRecyclerViewAdapter.OnListFragmentInteractionListener() {
                @Override
                public void onListFragmentInteraction(Producto item) {

                }
            }));

            return rootView;
        }
    }
}