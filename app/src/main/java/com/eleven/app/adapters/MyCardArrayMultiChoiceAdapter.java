package com.eleven.app.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.eleven.app.R;
import com.eleven.app.events.ConfirmEvent;
import com.eleven.app.events.CourseActionModeEvent;
import com.eleven.app.framgents.ConfirmDialog;
import com.eleven.app.models.Course;
import com.eleven.app.models.CourseManager;
import com.eleven.app.util.App;
import com.eleven.app.view.CourseCard;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayMultiChoiceAdapter;
import it.gmariotti.cardslib.library.view.CardView;

/**
 * Created by skyhacker on 14-2-26.
 */
public class MyCardArrayMultiChoiceAdapter extends CardArrayMultiChoiceAdapter {

    private Context mContext;
    private Fragment mFrament;
    private ActionMode mActionMode;

    public MyCardArrayMultiChoiceAdapter(Context context, Fragment fragment, List<Card> cards) {
        super(context, cards);
        this.mContext = context;
        this.mFrament = fragment;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        super.onCreateActionMode(mode, menu);
        App.getBus().register(this);
        App.getBus().post(new CourseActionModeEvent(true));
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        mode.getMenu().clear();
        ArrayList<Card> items = getSelectedCards();
        if (items.size() == 1) {
            mode.getMenuInflater().inflate(R.menu.edit, menu);
        }
        mode.getMenuInflater().inflate(R.menu.delete, menu);
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        mActionMode = mode;
        int itemId = item.getItemId();
        if (itemId == R.id.action_delete) {
            discardSelectedItems();
            return true;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        super.onDestroyActionMode(mode);
        App.getBus().post(new CourseActionModeEvent(false));
        App.getBus().unregister(this);
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode actionMode, int position, long id, boolean checked, CardView cardView, Card card) {
        actionMode.invalidate();
    }


    private void discardSelectedItems() {
        ArrayList<Card> items = getSelectedCards();
        ConfirmDialog dialog = new ConfirmDialog("确认删除这" + items.size() + "项", ConfirmEvent.OK);
        dialog.show(mFrament.getFragmentManager(), "confirm");
        //CourseManager.deleteCourse(courses);
    }

    @Subscribe public void onConfirmEvent(ConfirmEvent event) {
        if (event.getMsg() == ConfirmEvent.OK) {
            ArrayList<Card> items = getSelectedCards();
            for (Card item : items) {
                Course course = ((CourseCard) item).getCourse();
                CourseManager.deleteCourse(course);
                //App.getDBHelper().deleteCourse(course.getId());
                remove(item);
            }
            Toast.makeText(mContext, "删除成功！", Toast.LENGTH_SHORT).show();
        }
        mActionMode.finish();
    }


}
