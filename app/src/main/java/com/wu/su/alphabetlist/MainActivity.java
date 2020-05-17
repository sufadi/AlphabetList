package com.wu.su.alphabetlist;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wu.su.alphabetlist.adapter.AlphabetSortAdapter;
import com.wu.su.alphabetlist.domain.SortModel;
import com.wu.su.alphabetlist.utils.CharacterParser;
import com.wu.su.alphabetlist.widget.SideBarView;
import com.wu.su.alphabetlist.widget.SideBarView.OnTouchingLetterChangedListener;

public class MainActivity extends Activity {

    private final static String TAG = "shz_debug";
    private static final int MSG_HIDE_DIALOG = 0;

    private boolean isInputMethodShow;
    private PinyinComparator mPinyinComparator;
    private AlphabetSortAdapter mAlphabetAadpter;
    private H mHandle = new H(this);

    private List<SortModel> sourceDataList;
    // 中文转拼音工具栏（字母列表实现的核心工具栏）
    private CharacterParser mCharacterParser;

    private TextView tv_dialog;
    private EditText et_searchview;
    private ListView sortListView;
    private SideBarView SideBarView;

    private static class H extends Handler {
        WeakReference<MainActivity> mActivity;

        public H(MainActivity activity) {
            mActivity = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_HIDE_DIALOG:
                    mActivity.get().hideDialog();
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initValues();
        initListeners();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void initViews() {
        sortListView = (ListView) findViewById(R.id.name_listview);
        SideBarView = (SideBarView) findViewById(R.id.sidrbar);
        tv_dialog = (TextView) findViewById(R.id.tv_dialog);
        et_searchview = (EditText) findViewById(R.id.et_searchview);
    }

    private void initValues() {
        mCharacterParser = new CharacterParser();
        mPinyinComparator = new PinyinComparator();
        sourceDataList = loadFakeData(getResources().getStringArray(R.array.fake_data));
        // 根据a-z进行排序源数据
        Collections.sort(sourceDataList, mPinyinComparator);
        mAlphabetAadpter = new AlphabetSortAdapter(this, sourceDataList);
        sortListView.setAdapter(mAlphabetAadpter);
    }

    private void initListeners() {
        sortListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // 这里要利用adapter.getItem(position)来获取当前position所对应的对象
                Toast.makeText(getApplication(),
                        ((SortModel) mAlphabetAadpter.getItem(position)).info,
                        Toast.LENGTH_SHORT).show();
            }
        });

        sortListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                        //手指用力滑动
                        //手指离开listview后由于惯性继续滑动
                        showDialog();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) { }
        });

        sortListView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int positon = sortListView.getFirstVisiblePosition();
                String alpha = mAlphabetAadpter.getAlpha(positon);
                SideBarView.setCurrCharacter(alpha);
                tv_dialog.setText(alpha);
                Log.d(TAG, "onScrollChange positon:" + positon + ", alpha:" + alpha);
            }
        });

        sortListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }
        });

        // 设置右侧触摸监听
        SideBarView.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                Log.d(TAG, "onTouchingLetterChanged:" + s);
                int position = mAlphabetAadpter.getPositionForSection(s.charAt(0));
                tv_dialog.setText(s);
                showDialog();
                if (position != -1) {
                    sortListView.setSelection(position);
                }
            }
        });

        // 根据输入框输入值的改变来过滤搜索
        et_searchview.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                startSearch(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        et_searchview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isInputMethodShow = true;
                return false;
            }
        });
    }

    // 加载模拟数据
    private List<SortModel> loadFakeData(String[] data) {
        List<SortModel> mSortList = new ArrayList<SortModel>();
        for (int i = 0; i < data.length; i++) {

            SortModel sortModel = new SortModel();
            sortModel.info = data[i];
            sortModel.fistLetter = mCharacterParser.getSortKey(data[i]);
            mSortList.add(sortModel);
        }
        return mSortList;
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public void hideKeyboard() {
        if (getCurrentFocus() == null || !isInputMethodShow) {
            return;
        }
        isInputMethodShow = false;
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(
                Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }


    /**
     * 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
     */
    private void startSearch(String filterStr) {
        List<SortModel> startSearchList = new ArrayList<SortModel>();
        if (TextUtils.isEmpty(filterStr)) {
            startSearchList = sourceDataList;
        } else {
            startSearchList.clear();
            for (SortModel sortModel : sourceDataList) {
                String name = sortModel.info;
                if (name.toUpperCase().indexOf(filterStr.toString().toUpperCase()) != -1
                        || mCharacterParser.getPinYin(name).toUpperCase().contains(filterStr.toString().toUpperCase())) {
                    startSearchList.add(sortModel);
                }
            }
        }

        Collections.sort(startSearchList, mPinyinComparator);
        mAlphabetAadpter.updateListView(startSearchList);
    }

    // 根据a-z进行排序
    public class PinyinComparator implements Comparator<SortModel> {

        public int compare(SortModel o1, SortModel o2) {

            if (o1.fistLetter.equals("#")) {
                return -1;
            } else {
                return o1.fistLetter.compareTo(o2.fistLetter);
            }
        }
    }

    // 只显示 1 秒的字母对话框
    private void showDialog() {
        tv_dialog.setVisibility(View.VISIBLE);
        mHandle.removeMessages(MSG_HIDE_DIALOG);
        mHandle.sendEmptyMessageDelayed(MSG_HIDE_DIALOG, 1000);
    }

    private void hideDialog() {
        tv_dialog.setVisibility(View.GONE);
    }
}

