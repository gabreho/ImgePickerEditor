package com.skrumble.picketeditor.picker.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.otaliastudios.cameraview.CameraException;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraOptions;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.Facing;
import com.otaliastudios.cameraview.Flash;
import com.otaliastudios.cameraview.Gesture;
import com.otaliastudios.cameraview.GestureAction;
import com.otaliastudios.cameraview.SessionType;
import com.skrumble.picketeditor.CircularProgressBar;
import com.skrumble.picketeditor.Config;
import com.skrumble.picketeditor.PickerEditor;
import com.skrumble.picketeditor.R;
import com.skrumble.picketeditor.gallery.GalleryActivity;
import com.skrumble.picketeditor.picker.adapters.InstantImageAdapter;
import com.skrumble.picketeditor.picker.adapters.MainImageAdapter;
import com.skrumble.picketeditor.picker.interfaces.OnSelectionListener;
import com.skrumble.picketeditor.picker.modals.Img;
import com.skrumble.picketeditor.picker.public_interface.BitmapCallback;
import com.skrumble.picketeditor.picker.utility.Constants;
import com.skrumble.picketeditor.picker.utility.HeaderItemDecoration;
import com.skrumble.picketeditor.picker.utility.ImageVideoFetcher;
import com.skrumble.picketeditor.picker.utility.Utility;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class CameraActivity extends AppCompatActivity implements View.OnTouchListener {

    public static String EXTRA_CAMERA_TYPE = "EXTRA_CAMERA_TYPE";
    public static int ARG_CAMERA_TYPE_PICTURE = 0;
    public static int ARG_CAMERA_TYPE_VIDEO = 1;

    // Camera Type
    private int cameraType = 0;

    // Constants
    private static final int sBubbleAnimDuration = 1000;
    private static final int sScrollbarHideDelay = 1000;
    public static final String SELECTION = "selection";
    private static final int sTrackSnapRange = 5;

    // Variables
    public static float TOPBAR_HEIGHT;
    private int BottomBarHeight = 0;
    private int colorPrimaryDark;
    private float zoom = 0.0f;
    private float dist = 0.0f;
    private float mViewHeight;
    private boolean mHideScrollbar = true;
    private boolean LongSelection = false;
    private int SelectionCount = 1;
    private boolean isback = true;
    private int flashDrawable;
    private Set<Img> selectionList = new HashSet<>();

    // Views
    private CameraView cameraView;
    private RecyclerView recyclerView, instantRecyclerView;
    private View status_bar_bg, mScrollbar, topbar, bottomButtons, sendButton;
    private View mBottomSheet;
    private TextView mBubbleView, img_count, cameraViewTip;
    private TextView selection_count;
    private ImageView mHandleView, selection_back, selection_check, captureButton, cameraFacingButton, flashButton;
    private CircularProgressBar mCircularProgressBar;

    // Adapters
    private InstantImageAdapter initaliseadapter;
    private MainImageAdapter mainImageAdapter;

    // Handlers
    private Handler handler = new Handler();

    // Animators
    private ViewPropertyAnimator mScrollbarAnimator;
    private ViewPropertyAnimator mBubbleAnimator;

    // Behavior
    private BottomSheetBehavior mBottomSheetBehavior;

    // Runnables
    private Runnable mScrollbarHider = new Runnable() {
        @Override
        public void run() {
            hideScrollbar();
        }
    };

    private CountDownTimer countDownTimer;

    // *********************************************************************************************
    // region Life Cycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.setupStatusBarHidden(this);
        Utility.hideStatusBar(this);
        setContentView(R.layout.activity_main_lib);

        if (getIntent() != null){
            cameraType = getIntent().getIntExtra(EXTRA_CAMERA_TYPE, 0);
        }

        initialize();

        if (cameraType == ARG_CAMERA_TYPE_VIDEO){
            cameraView.setSessionType(SessionType.VIDEO);
            instantRecyclerView.setVisibility(View.GONE);
            cameraFacingButton.setVisibility(View.GONE);
            flashButton.setVisibility(View.GONE);
            cameraViewTip.setVisibility(View.GONE);
            captureButton.setVisibility(View.VISIBLE);
            mCircularProgressBar.setVisibility(View.GONE);
            captureButton.setImageResource(R.drawable.ic_and_icn_rec_video);
        }else {
            captureButton.setVisibility(View.VISIBLE);
            mCircularProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initialize();
        cameraView.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    protected void onPause() {
        cameraView.stop();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (selectionList.size() > 0) {
            for (Img img : selectionList) {
                mainImageAdapter.getItemList().get(img.getPosition()).setSelected(false);
                mainImageAdapter.notifyItemChanged(img.getPosition());
                initaliseadapter.getItemList().get(img.getPosition()).setSelected(false);
                initaliseadapter.notifyItemChanged(img.getPosition());
            }
            LongSelection = false;
            if (SelectionCount > 1) {
                selection_check.setVisibility(View.VISIBLE);
            }
            DrawableCompat.setTint(selection_back.getDrawable(), colorPrimaryDark);
            topbar.setBackgroundColor(Color.parseColor("#ffffff"));
            Animation anim = new ScaleAnimation(
                    1f, 0f, // Start and end values for the X axis scaling
                    1f, 0f, // Start and end values for the Y axis scaling
                    Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                    Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
            anim.setFillAfter(true); // Needed to keep the result of the animation
            anim.setDuration(300);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    sendButton.setVisibility(View.GONE);
                    sendButton.clearAnimation();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            sendButton.startAnimation(anim);
            selectionList.clear();
        } else if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    // endregion

    private void hideScrollbar() {
        float transX = getResources().getDimensionPixelSize(R.dimen.fastscroll_scrollbar_padding_end);
        mScrollbarAnimator = mScrollbar.animate().translationX(transX).alpha(0f)
                .setDuration(Constants.sScrollbarAnimDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mScrollbar.setVisibility(View.GONE);
                        mScrollbarAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        super.onAnimationCancel(animation);
                        mScrollbar.setVisibility(View.GONE);
                        mScrollbarAnimator = null;
                    }
                });
    }

    public void returnObjects() {
        ArrayList<String> list = new ArrayList<>();
        for (Img i : selectionList) {
            list.add(i.getUrl());
            Log.e(CameraActivity.class.getSimpleName() + " images", "img " + i.getUrl());
        }

        Img next = selectionList.iterator().next();

        PickerEditor.starEditor(this, next.getUrl());
    }

    private void initialize() {
        Utility.getScreenSize(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        try {
            SelectionCount = getIntent().getIntExtra(SELECTION, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Setting Views
        cameraView = findViewById(R.id.camera_view);
        sendButton = findViewById(R.id.sendButton);
        img_count = findViewById(R.id.img_count);
        mBubbleView = findViewById(R.id.fastscroll_bubble);
        mHandleView = findViewById(R.id.fastscroll_handle);
        mScrollbar = findViewById(R.id.fastscroll_scrollbar);
        topbar = findViewById(R.id.topbar);
        selection_count = findViewById(R.id.selection_count);
        selection_back = findViewById(R.id.selection_back);
        selection_check = findViewById(R.id.selection_check);
        bottomButtons = findViewById(R.id.bottomButtons);
        status_bar_bg = findViewById(R.id.status_bar_bg);
        instantRecyclerView = findViewById(R.id.instantRecyclerView);
        recyclerView = findViewById(R.id.recyclerView);
        captureButton = findViewById(R.id.clickme);
        cameraViewTip = findViewById(R.id.camera_view_tip);
        cameraFacingButton = findViewById(R.id.front);
        flashButton = findViewById(R.id.flash);
        mCircularProgressBar = findViewById(R.id.record_circular_progress_bar);

        mCircularProgressBar.setMaxValue(Config.MAX_VIDEO_RECORDING_LENGTH);
        mCircularProgressBar.setProgress(0);

        FrameLayout mainFrameLayout = findViewById(R.id.mainFrameLayout);

        //Layout Managers
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, MainImageAdapter.SPAN_COUNT);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mainImageAdapter.getItemViewType(position) == MainImageAdapter.HEADER) {
                    return MainImageAdapter.SPAN_COUNT;
                }
                return 1;
            }
        });

        // Set Layout Managers
        instantRecyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setLayoutManager(gridLayoutManager);

        // Adapters
        initaliseadapter = new InstantImageAdapter(this);
        initaliseadapter.addOnSelectionListener(onSelectionListener);
        instantRecyclerView.setAdapter(initaliseadapter);

        mainImageAdapter = new MainImageAdapter(this);
        mainImageAdapter.addOnSelectionListener(onSelectionListener);

        // Set Adapters
        recyclerView.setAdapter(mainImageAdapter);

        // Layout Params
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        lp.setMargins(0, 0, 0, BottomBarHeight);

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) sendButton.getLayoutParams();
        layoutParams.setMargins(0, 0, (int) (Utility.convertDpToPixel(16, this)),
                (int) (Utility.convertDpToPixel(174, this)));

        // Set Layout Params
        mainFrameLayout.setLayoutParams(lp);
        sendButton.setLayoutParams(layoutParams);

        // Set Visibility
        selection_check.setVisibility((SelectionCount > 1) ? View.VISIBLE : View.GONE);
        mScrollbar.setVisibility(View.GONE);
        mBubbleView.setVisibility(View.GONE);

        // Set Variables
        BottomBarHeight = Utility.getSoftButtonsBarSizePort(this);
        flashDrawable = R.drawable.ic_flash_off_black_24dp;
        status_bar_bg.setBackgroundColor(Color.BLACK);

        TOPBAR_HEIGHT = Utility.convertDpToPixel(56, CameraActivity.this);
        mHandleView.setOnTouchListener(this);
        recyclerView.addOnScrollListener(mScrollListener);
        recyclerView.addItemDecoration(new HeaderItemDecoration(this, mainImageAdapter));
        DrawableCompat.setTint(selection_back.getDrawable(), colorPrimaryDark);

        zoom = 0.0f;
        cameraView.setLifecycleOwner(this);
        cameraView.setZoom(zoom);
        cameraView.mapGesture(Gesture.PINCH, GestureAction.ZOOM);
        cameraView.mapGesture(Gesture.TAP, GestureAction.FOCUS_WITH_MARKER);
        cameraView.mapGesture(Gesture.LONG_TAP, GestureAction.CAPTURE);
        cameraView.setVideoMaxDuration(Config.MAX_VIDEO_RECORDING_LENGTH);

        // View Methods
        onClickMethods();
        updateImages();

        countDownTimer = new CountDownTimer(Config.MAX_VIDEO_RECORDING_LENGTH, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                mCircularProgressBar.setProgress(millisUntilFinished);
            }

            @Override
            public void onFinish() {

            }
        };
    }

    @SuppressLint("ClickableViewAccessibility")
    private void onClickMethods() {

        cameraFacingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraFacingClickAction(v);
            }
        });

        flashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashClickAction(v);
            }
        });

        findViewById(R.id.selection_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CameraActivity.this, "fin", Toast.LENGTH_SHORT).show();
                //Log.e("Hello", "onclick");
                returnObjects();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CameraActivity.this, "fin", Toast.LENGTH_SHORT).show();
                //Log.e("Hello", "onclick");
                returnObjects();
            }
        });

        selection_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        selection_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                topbar.setBackgroundColor(colorPrimaryDark);
                selection_count.setText(getResources().getString(R.string.tap_to_select));
                img_count.setText(String.valueOf(selectionList.size()));
                DrawableCompat.setTint(selection_back.getDrawable(), Color.parseColor("#ffffff"));
                LongSelection = true;
                selection_check.setVisibility(View.GONE);
            }
        });

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureButtonClickAction();
            }
        });
    }

    private void setBottomSheetBehavior() {
        mBottomSheet = findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
        mBottomSheetBehavior.setPeekHeight((int) (Utility.convertDpToPixel(194, this)));
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {

            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                Utility.manipulateVisibility(CameraActivity.this, slideOffset,
                        instantRecyclerView, recyclerView, status_bar_bg,
                        topbar, bottomButtons, sendButton, LongSelection);
                if (slideOffset == 1) {
                    Utility.showScrollbar(mScrollbar, CameraActivity.this);
                    mainImageAdapter.notifyDataSetChanged();
                    mViewHeight = mScrollbar.getMeasuredHeight();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            setViewPositions(getScrollProportion(recyclerView));
                        }
                    });
                    sendButton.setVisibility(View.GONE);

                } else if (slideOffset == 0) {

                    initaliseadapter.notifyDataSetChanged();
                    hideScrollbar();
                    img_count.setText(String.valueOf(selectionList.size()));

                    cameraView.start();
                }
            }
        });
    }

    private float getScrollProportion(RecyclerView recyclerView) {
        final int verticalScrollOffset = recyclerView.computeVerticalScrollOffset();
        final int verticalScrollRange = recyclerView.computeVerticalScrollRange();
        final float rangeDiff = verticalScrollRange - mViewHeight;
        float proportion = (float) verticalScrollOffset / (rangeDiff > 0 ? rangeDiff : 1f);
        return mViewHeight * proportion;
    }

    private void setViewPositions(float y) {
        int handleY = Utility.getValueInRange(0, (int) (mViewHeight - mHandleView.getHeight()), (int) (y - mHandleView.getHeight() / 2));
        mBubbleView.setY(handleY + Utility.convertDpToPixel((56), CameraActivity.this));
        mHandleView.setY(handleY);
    }

    private void setRecyclerViewPosition(float y) {
        if (recyclerView != null && recyclerView.getAdapter() != null) {
            int itemCount = recyclerView.getAdapter().getItemCount();
            float proportion;

            if (mHandleView.getY() == 0) {
                proportion = 0f;
            } else if (mHandleView.getY() + mHandleView.getHeight() >= mViewHeight - sTrackSnapRange) {
                proportion = 1f;
            } else {
                proportion = y / mViewHeight;
            }

            int scrolledItemCount = Math.round(proportion * itemCount);
            int targetPos = Utility.getValueInRange(0, itemCount - 1, scrolledItemCount);
            recyclerView.getLayoutManager().scrollToPosition(targetPos);

            if (mainImageAdapter != null) {
                String text = mainImageAdapter.getSectionMonthYearText(targetPos);
                mBubbleView.setText(text);
                if (text.equalsIgnoreCase("")) {
                    mBubbleView.setVisibility(View.GONE);
                }
            }
        }
    }

    // *********************************************************************************************
    // region Bubble

    private void showBubble() {
        if (!Utility.isViewVisible(mBubbleView)) {
            mBubbleView.setVisibility(View.VISIBLE);
            mBubbleView.setAlpha(0f);
            mBubbleAnimator = mBubbleView.animate().alpha(1f)
                    .setDuration(sBubbleAnimDuration)
                    .setListener(new AnimatorListenerAdapter() {
                        // adapter required for new alpha value to stick
                    });
            mBubbleAnimator.start();
        }
    }

    private void hideBubble() {
        if (Utility.isViewVisible(mBubbleView)) {
            mBubbleAnimator = mBubbleView.animate().alpha(0f)
                    .setDuration(sBubbleAnimDuration)
                    .setListener(new AnimatorListenerAdapter() {

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mBubbleView.setVisibility(View.GONE);
                            mBubbleAnimator = null;
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                            super.onAnimationCancel(animation);
                            mBubbleView.setVisibility(View.GONE);
                            mBubbleAnimator = null;
                        }
                    });
            mBubbleAnimator.start();
        }
    }

    // endregion

    // *********************************************************************************************
    // region Fill Image

    @SuppressLint("StaticFieldLeak")
    private void updateImages() {
        new ImageVideoFetcher(CameraActivity.this) {
            @Override
            protected void onPostExecute(ArrayList<Img> imgs) {
                super.onPostExecute(imgs);
                initaliseadapter.addImageList(imgs);
                mainImageAdapter.addImageList(imgs);
                setBottomSheetBehavior();
            }
        }.execute(Constants.TYPE_IMAGE);
    }


    // endregion

    // *********************************************************************************************
    // region Click Action

    public void cameraFacingClickAction(View view){

        final ImageView front = (ImageView) view;

        final ObjectAnimator oa1 = ObjectAnimator.ofFloat(front, "scaleX", 1f, 0f).setDuration(150);
        final ObjectAnimator oa2 = ObjectAnimator.ofFloat(front, "scaleX", 0f, 1f).setDuration(150);

        oa1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                front.setImageResource(R.drawable.ic_photo_camera);
                oa2.start();
            }
        });

        oa1.start();

        if (isback) {
            isback = false;
            cameraView.setFacing(Facing.FRONT);
        } else {
            isback = true;
            cameraView.setFacing(Facing.BACK);
        }
    }

    public void flashClickAction(final View view){
        final int height = view.getHeight();

        final ImageView imageView = (ImageView) view;

        imageView.animate().translationY(height).setDuration(100).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                imageView.setTranslationY(-(height / 2));

                if (flashDrawable == R.drawable.ic_flash_auto_black_24dp) {
                    flashDrawable = R.drawable.ic_flash_off_black_24dp;
                    imageView.setImageResource(flashDrawable);
                    cameraView.setFlash(Flash.OFF);

                } else if (flashDrawable == R.drawable.ic_flash_off_black_24dp) {
                    flashDrawable = R.drawable.ic_flash_on_black_24dp;
                    imageView.setImageResource(flashDrawable);
                    cameraView.setFlash(Flash.ON);

                } else {
                    flashDrawable = R.drawable.ic_flash_auto_black_24dp;
                    imageView.setImageResource(flashDrawable);
                    cameraView.setFlash(Flash.AUTO);
                }

                imageView.animate().translationY(0).setDuration(50).setListener(null).start();
            }
        }).start();
    }

    // endregion

    // *********************************************************************************************
    // region Listeners

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getX() < mHandleView.getX() - ViewCompat.getPaddingStart(mHandleView)) {
                    return false;
                }
                mHandleView.setSelected(true);
                handler.removeCallbacks(mScrollbarHider);
                Utility.cancelAnimation(mScrollbarAnimator);
                Utility.cancelAnimation(mBubbleAnimator);

                if (!Utility.isViewVisible(mScrollbar) && (recyclerView.computeVerticalScrollRange() - mViewHeight > 0)) {
                    mScrollbarAnimator = Utility.showScrollbar(mScrollbar, CameraActivity.this);
                }

                if (mainImageAdapter != null) {
                    showBubble();
                }

            case MotionEvent.ACTION_MOVE:
                final float y = event.getRawY();
             /*   String text = mainImageAdapter.getSectionText(recyclerView.getVerticalScrollbarPosition()).trim();
                mBubbleView.setText("hello------>"+text+"<--");
                if (text.equalsIgnoreCase("")) {
                    mBubbleView.setVisibility(View.GONE);
                }
                Log.e("hello"," -->> "+ mBubbleView.getText());*/
                setViewPositions(y - TOPBAR_HEIGHT);
                setRecyclerViewPosition(y);
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mHandleView.setSelected(false);
                if (mHideScrollbar) {
                    handler.postDelayed(mScrollbarHider, sScrollbarHideDelay);
                }
                hideBubble();

                return true;
        }
        return super.onTouchEvent(event);
    }

    private CameraListener mCameraListener = new CameraListener() {
        @Override
        public void onCameraOpened(CameraOptions options) {
            super.onCameraOpened(options);
        }

        @Override
        public void onCameraClosed() {
            super.onCameraClosed();
        }

        @Override
        public void onCameraError(@NonNull CameraException exception) {
            super.onCameraError(exception);
        }

        @Override
        public void onVideoTaken(File video) {
            super.onVideoTaken(video);
        }

        @Override
        public void onOrientationChanged(int orientation) {
            super.onOrientationChanged(orientation);
        }

        @Override
        public void onFocusStart(PointF point) {
            super.onFocusStart(point);
        }

        @Override
        public void onFocusEnd(boolean successful, PointF point) {
            super.onFocusEnd(successful, point);
        }

        @Override
        public void onZoomChanged(float newValue, float[] bounds, PointF[] fingers) {
            super.onZoomChanged(newValue, bounds, fingers);
        }

        @Override
        public void onExposureCorrectionChanged(float newValue, float[] bounds, PointF[] fingers) {
            super.onExposureCorrectionChanged(newValue, bounds, fingers);
        }
    };

    private RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (!mHandleView.isSelected() && recyclerView.isEnabled()) {
                setViewPositions(getScrollProportion(recyclerView));
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            if (recyclerView.isEnabled()) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        handler.removeCallbacks(mScrollbarHider);
                        Utility.cancelAnimation(mScrollbarAnimator);
                        if (!Utility.isViewVisible(mScrollbar) && (recyclerView.computeVerticalScrollRange() - mViewHeight > 0)) {
                            mScrollbarAnimator = Utility.showScrollbar(mScrollbar, CameraActivity.this);
                        }
                        break;
                    case RecyclerView.SCROLL_STATE_IDLE:
                        if (mHideScrollbar && !mHandleView.isSelected()) {
                            handler.postDelayed(mScrollbarHider, sScrollbarHideDelay);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    };

    private OnSelectionListener onSelectionListener = new OnSelectionListener() {
        @Override
        public void onClick(Img img, View view, int position) {
            //Log.e("onClick", "onClick");
            if (LongSelection) {
                if (selectionList.contains(img)) {
                    selectionList.remove(img);
                    initaliseadapter.select(false, position);
                    mainImageAdapter.select(false, position);
                } else {
                    if (SelectionCount <= selectionList.size()) {
                        Toast.makeText(CameraActivity.this, String.format(getResources().getString(R.string.selection_limiter), selectionList.size()), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    img.setPosition(position);
                    selectionList.add(img);
                    initaliseadapter.select(true, position);
                    mainImageAdapter.select(true, position);
                }
                if (selectionList.size() == 0) {
                    LongSelection = false;
                    selection_check.setVisibility(View.VISIBLE);
                    DrawableCompat.setTint(selection_back.getDrawable(), colorPrimaryDark);
                    topbar.setBackgroundColor(Color.parseColor("#ffffff"));
                    Animation anim = new ScaleAnimation(
                            1f, 0f, // Start and end values for the X axis scaling
                            1f, 0f, // Start and end values for the Y axis scaling
                            Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                            Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
                    anim.setFillAfter(true); // Needed to keep the result of the animation
                    anim.setDuration(300);
                    anim.setAnimationListener(new Animation.AnimationListener() {

                        /**
                         * <p>Notifies the start of the animation.</p>
                         *
                         * @param animation The started animation.
                         */
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            sendButton.setVisibility(View.GONE);
                            sendButton.clearAnimation();
                        }

                        /**
                         * <p>Notifies the repetition of the animation.</p>
                         *
                         * @param animation The animation which was repeated.
                         */
                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    sendButton.startAnimation(anim);

                }
                selection_count.setText(getResources().getString(R.string.selected) + " " + selectionList.size());
                img_count.setText(String.valueOf(selectionList.size()));
            } else {
                img.setPosition(position);
                selectionList.add(img);
                returnObjects();
                DrawableCompat.setTint(selection_back.getDrawable(), colorPrimaryDark);
                topbar.setBackgroundColor(Color.parseColor("#ffffff"));
            }
        }

        @Override
        public void onLongClick(Img img, View view, int position) {
            if (SelectionCount > 1) {
                Utility.vibe(CameraActivity.this, 50);
                //Log.e("onLongClick", "onLongClick");
                LongSelection = true;
                if ((selectionList.size() == 0) && (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED)) {
                    sendButton.setVisibility(View.VISIBLE);
                    Animation anim = new ScaleAnimation(
                            0f, 1f, // Start and end values for the X axis scaling
                            0f, 1f, // Start and end values for the Y axis scaling
                            Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                            Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
                    anim.setFillAfter(true); // Needed to keep the result of the animation
                    anim.setDuration(300);
                    sendButton.startAnimation(anim);
                }
                if (selectionList.contains(img)) {
                    selectionList.remove(img);
                    initaliseadapter.select(false, position);
                    mainImageAdapter.select(false, position);
                } else {
                    img.setPosition(position);
                    selectionList.add(img);
                    initaliseadapter.select(true, position);
                    mainImageAdapter.select(true, position);
                }
                selection_check.setVisibility(View.GONE);
                topbar.setBackgroundColor(colorPrimaryDark);
                selection_count.setText(getResources().getString(R.string.selected) + " " + selectionList.size());
                img_count.setText(String.valueOf(selectionList.size()));
                DrawableCompat.setTint(selection_back.getDrawable(), Color.parseColor("#ffffff"));
            }

        }
    };

    private View.OnTouchListener onCameraTouchListner = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getPointerCount() > 1) {

                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_POINTER_DOWN:
                        dist = Utility.getFingerSpacing(event);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float maxZoom = 1f;

                        float newDist = Utility.getFingerSpacing(event);
                        if (newDist > dist) {
                            //zoom in
                            if (zoom < maxZoom)
                                zoom = zoom + 0.01f;
                        } else if ((newDist < dist) && (zoom > 0)) {
                            //zoom out
                            zoom = zoom - 0.01f;
                        }
                        dist = newDist;
                        cameraView.setZoom(zoom);
                        break;
                    default:
                        break;
                }
            }
            return true;
        }
    };

    // endregion

    // *********************************************************************************************
    // region capture or record

    private void captureButtonClickAction() {
        if (cameraType == ARG_CAMERA_TYPE_VIDEO){
            recordVideo();
            return;
        }

        capturePicture();
    }

    private void capturePicture(){
        // Capture Picture
        cameraView.addCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(byte[] jpeg) {

                Utility.decodeBitmap(jpeg, new BitmapCallback() {
                    @Override
                    public void onBitmapReady(Bitmap bitmap) {
                        File file = Utility.writeImageToCatchFolder(bitmap, CameraActivity.this);
                        if (file != null && file.exists()){
                            PickerEditor.starEditor(CameraActivity.this, file.getAbsolutePath());
                        }
                    }
                });
            }
        });

        cameraView.capturePicture();
    }

    private void recordVideo(){

        if (cameraView.isCapturingVideo()){
            stopRecording();
            return;
        }

        File videoFileInCatchFolder = Utility.getVideoFileInCatchFolder(this);
        cameraView.setSessionType(SessionType.VIDEO);

        cameraView.addCameraListener(new CameraListener() {
            @Override
            public void onVideoTaken(File video) {
                PickerEditor.starVideoEditor(CameraActivity.this, video.getAbsolutePath());
            }
        });

        captureButton.setImageResource(R.drawable.ic_and_icn_rec_stop);
        mCircularProgressBar.setVisibility(View.VISIBLE);
        cameraView.startCapturingVideo(videoFileInCatchFolder);

        instantRecyclerView.setVisibility(View.GONE);
        cameraViewTip.setVisibility(View.GONE);
        flashButton.setVisibility(View.GONE);
        cameraFacingButton.setVisibility(View.GONE);

        countDownTimer = new CountDownTimer(Config.MAX_VIDEO_RECORDING_LENGTH, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                mCircularProgressBar.setProgress(Config.MAX_VIDEO_RECORDING_LENGTH - millisUntilFinished);
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    private void stopRecording(){
        if (cameraView.isCapturingVideo() == false){
            return;
        }

        cameraView.stopCapturingVideo();
    }
    // endregion

}