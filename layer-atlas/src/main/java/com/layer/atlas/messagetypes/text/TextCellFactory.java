package com.layer.atlas.messagetypes.text;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.text.style.URLSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.layer.atlas.R;
import com.layer.atlas.UrlCrawler.LinkPreviewCallback;
import com.layer.atlas.UrlCrawler.SourceContent;
import com.layer.atlas.UrlCrawler.TextCrawler;
import com.layer.atlas.messagetypes.AtlasCellFactory;
import com.layer.atlas.provider.Participant;
import com.layer.atlas.provider.ParticipantProvider;
import com.layer.atlas.util.Util;
import com.layer.sdk.LayerClient;
import com.layer.sdk.messaging.Actor;
import com.layer.sdk.messaging.Message;
import com.layer.sdk.messaging.MessagePart;

public class TextCellFactory extends AtlasCellFactory<TextCellFactory.CellHolder, TextCellFactory.TextInfo> implements View.OnLongClickListener {
    public final static String MIME_TYPE = "text/plain";

    public TextCellFactory() {
        super(256 * 1024);
    }

    public static boolean isType(Message message) {
        return message.getMessageParts().get(0).getMimeType().equals(MIME_TYPE);
    }

    public static String getMessagePreview(Context context, Message message) {
        MessagePart part = message.getMessageParts().get(0);
        // For large text content, the MessagePart may not be downloaded yet.
        return part.isContentReady() ? new String(part.getData()) : "";
    }

    @Override
    public boolean isBindable(Message message) {
        return TextCellFactory.isType(message);
    }

    @Override
    public CellHolder createCellHolder(ViewGroup cellView, boolean isMe, LayoutInflater layoutInflater) {
        View v = layoutInflater.inflate(R.layout.atlas_message_item_cell_text, cellView, true);
        v.setBackgroundResource(isMe ? R.drawable.atlas_message_item_cell_me : R.drawable.atlas_message_item_cell_them);
        ((GradientDrawable) v.getBackground()).setColor(isMe ? mMessageStyle.getMyBubbleColor() : mMessageStyle.getOtherBubbleColor());

        TextView t = (TextView) v.findViewById(R.id.cell_text);
        t.setTextSize(TypedValue.COMPLEX_UNIT_PX, isMe ? mMessageStyle.getMyTextSize() : mMessageStyle.getOtherTextSize());
        t.setTextColor(isMe ? mMessageStyle.getMyTextColor() : mMessageStyle.getOtherTextColor());
        t.setLinkTextColor(isMe ? mMessageStyle.getMyTextColor() : mMessageStyle.getOtherTextColor());
        t.setTypeface(isMe ? mMessageStyle.getMyTextTypeface() : mMessageStyle.getOtherTextTypeface(), isMe ? mMessageStyle.getMyTextStyle() : mMessageStyle.getOtherTextStyle());
        return new CellHolder(v);
    }

    @Override
    public TextInfo parseContent(LayerClient layerClient, ParticipantProvider participantProvider, Message message) {
        MessagePart part = message.getMessageParts().get(0);
        String text = part.isContentReady() ? new String(part.getData()) : "";
        String name;
        Actor sender = message.getSender();
        if (sender.getName() != null) {
            name = sender.getName() + ": ";
        } else {
            Participant participant = participantProvider.getParticipant(sender.getUserId());
            name = participant == null ? "" : (participant.getName() + ": ");
        }
        return new TextInfo(text, name);
    }

    @Override
    public void bindCellHolder(final CellHolder cellHolder, final TextInfo parsed, Message message, CellHolderSpecs specs) {
        cellHolder.mTextView.setText(parsed.getString());
        cellHolder.mTextView.setTag(parsed);
        cellHolder.mTextView.setOnLongClickListener(this);

        URLSpan[] urlSpans = cellHolder.mTextView.getUrls();
        if(urlSpans!=null)
        {
            Log.e("*******", "urlSpans size "+ urlSpans.length +" text "+ parsed.getString());
            if(urlSpans.length>0)
            {
                cellHolder.rlShorLinkPreviewContainer.setVisibility(View.VISIBLE);

                    TextCrawler obj = new TextCrawler();
                    obj.makePreview(new LinkPreviewCallback() {
                    @Override
                    public void onPre() {

                    }

                    @Override
                    public void onPos(SourceContent sourceContent, boolean isNull) {
                        Log.e("*******", "title   "+ sourceContent.getTitle());

                                    cellHolder.tvLinkTitle.setText(sourceContent.getTitle());
//                            .setText(sourceContent.getCannonicalUrl());
                                    cellHolder.tvLinkDesc.setText(sourceContent.getDescription());
//
                    }
                }, "http://www.babychakra.com/learn/721-quick-and-easy-egg-recipes-baked-eggs");


//                new Handler().post(new Runnable() {
//                    @Override
//                    public void run() {
//                        TextCrawler objTextCrawler = new TextCrawler();
//                        objTextCrawler.makePreview(new LinkPreviewCallback() {
//                            @Override
//                            public void onPre() {
//                            }
//
//                            @Override
//                            public void onPos(SourceContent sourceContent, boolean isNull) {
//
//                                /** Removing the loading layout */
//
//                                Log.e("*******", "title  " + sourceContent.getTitle());
//
//                                if (isNull || sourceContent.getFinalUrl().equals("")) {
//                                    /**
//                                     * Inflating the content layout into Main View LinearLayout
//                                     */
//                                    cellHolder.rlShorLinkPreviewContainer.setVisibility(View.GONE);
//
//                                } else {
//
//                                    /**
//                                     * Inflating the content layout into Main View LinearLayout
//                                     */
//
//                                    if (sourceContent.getImages().size() > 0) {
//
//                                        if (sourceContent.getImages().size() > 1) {
//                                            String url = sourceContent.getImages().get(0);
//
//                                        }
//                                    }
//                                    if (sourceContent.getTitle().equals(""))
//                                        sourceContent.setTitle("Babychakra");
//                                    if (sourceContent.getDescription().equals(""))
//                                        sourceContent
//                                                .setDescription("babychakra");
//
//                                    cellHolder.tvLinkTitle.setText(sourceContent.getTitle());
////                            .setText(sourceContent.getCannonicalUrl());
//                                    cellHolder.tvLinkDesc.setText(sourceContent.getDescription());
//
////                            postButton.setVisibility(View.VISIBLE);
//                                }
//
////                        currentTitle = sourceContent.getTitle();
////                        currentDescription = sourceContent.getDescription();
////                        currentUrl = sourceContent.getUrl();
////                        currentCannonicalUrl = sourceContent.getCannonicalUrl();
//                            }
//
//                        }, parsed.toString().trim());
//                    }
//                });


            }
                else
                cellHolder.rlShorLinkPreviewContainer.setVisibility(View.GONE);

        }
        else {
            cellHolder.rlShorLinkPreviewContainer.setVisibility(View.GONE);
            Log.e("*******", "urlSpans size 0 "+ parsed.getString());
        }
    }

    /**
     * Long click copies message text and sender name to clipboard
     */
    @Override
    public boolean onLongClick(View v) {
        TextInfo parsed = (TextInfo) v.getTag();
        String text = parsed.getClipboardPrefix() + parsed.getString();
        Util.copyToClipboard(v.getContext(), R.string.atlas_text_cell_factory_clipboard_description, text);
        Toast.makeText(v.getContext(), R.string.atlas_text_cell_factory_copied_to_clipboard, Toast.LENGTH_SHORT).show();
        return true;
    }

    public static class CellHolder extends AtlasCellFactory.CellHolder {
        TextView mTextView;
        TextView tvLinkTitle;
        TextView tvLinkDesc;
        RelativeLayout rlShorLinkPreviewContainer;

        public CellHolder(View view) {
            mTextView = (TextView) view.findViewById(R.id.cell_text);
            tvLinkTitle = (TextView) view.findViewById(R.id.tvLinkTitle);
            tvLinkDesc = (TextView) view.findViewById(R.id.tvLinkDesc);
            rlShorLinkPreviewContainer = (RelativeLayout) view.findViewById(R.id.rlShorLinkPreviewContainer);
        }
    }

    public static class TextInfo implements AtlasCellFactory.ParsedContent {
        private final String mString;
        private final String mClipboardPrefix;
        private final int mSize;

        public TextInfo(String string, String clipboardPrefix) {
            mString = string;
            mClipboardPrefix = clipboardPrefix;
            mSize = mString.getBytes().length + mClipboardPrefix.getBytes().length;
        }

        public String getString() {
            return mString;
        }

        public String getClipboardPrefix() {
            return mClipboardPrefix;
        }

        @Override
        public int sizeOf() {
            return mSize;
        }
    }

}
