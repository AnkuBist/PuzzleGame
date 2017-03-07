package com.hgil.game.puzzle;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.Formatter;

/**
 * Congratulates the user on solving the puzzle.
 */
public class YouWin extends Activity implements OnClickListener {
    public YouWin() {
        super(R.layout.congrats);
    }

    public void onClick(View v) {
        choice(getResources().getString(R.string.text_next_puzzle));
    }

    @Override
    protected void onDialogResponse(int dialogId, int response) {
        if (CHOICE_DIALOG == dialogId) {
            if (DialogInterface.BUTTON_POSITIVE == response)
                setResult(RESULT_OK);
            else
                setResult(RESULT_CANCELED);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView stats = (TextView) findViewById(R.id.text_puzzle_solved);
        final Intent intent = getIntent();
        final int moveCount = intent.getIntExtra(EXTRA_MOVE_COUNT, 0);
        final String template = getResources().getString(R.string.text_puzzle_solved);
        Formatter format = new Formatter().format(template, moveCount, 1 == moveCount ? "" : "s");
        stats.setText((CharSequence) format.out());
        if (intent.hasExtra(ImageSelection.EXTRA_SELECTED_IMAGE_ID_KEY)) {
            initImageView(intent.getSerializableExtra(ImageSelection.EXTRA_SELECTED_IMAGE_ID_KEY));
            findViewById(R.id.congratulations_picture).setOnClickListener(this);
        } else {
            final View button = findViewById(R.id.next_puzzle_button);
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(this);
        }
        findViewById(R.id.text_puzzle_blurb_2).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Uri url = Uri.parse(getResources().getString(R.string.text_puzzle_blurb_url));
                        Intent call = new Intent(Intent.ACTION_VIEW, url);
                        startActivity(call);
                    }
                }
        );
    }

    protected void initImageView(final Serializable imageId) {
        final ImageView imageView = (ImageView) findViewById(R.id.congratulations_picture);
        imageView.setVisibility(View.VISIBLE);
        if (imageId instanceof Integer)
            imageView.setImageResource((Integer) imageId);
        else
            imageView.setImageURI(Uri.fromFile(
                    ImageSource.imageFileForId((String) imageId, this)));
    }

    protected static final String EXTRA_MOVE_COUNT = "move_count";
    protected static final String EXTRA_IMAGE = "image_cache";
}
