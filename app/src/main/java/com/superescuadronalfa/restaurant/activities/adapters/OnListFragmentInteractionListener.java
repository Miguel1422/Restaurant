package com.superescuadronalfa.restaurant.activities.adapters;

import com.superescuadronalfa.restaurant.dbEntities.Mesa;

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
