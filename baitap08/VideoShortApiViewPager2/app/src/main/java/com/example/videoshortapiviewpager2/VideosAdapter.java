package com.example.videoshortapiviewpager2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideoViewHolder> {

    private Context context;
    private List<VideoModel> videoList;

    public VideosAdapter(Context context, List<VideoModel> videoList) {
        this.context = context;
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_video_row, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        VideoModel video = videoList.get(position);

        // Cài đặt tiêu đề và mô tả video
        holder.textVideoTitle.setText(video.getTitle());
        holder.textVideoDescription.setText(video.getDescription());

        // Hiển thị ProgressBar trong khi WebView tải nội dung
        holder.videoProgressBar.setVisibility(View.VISIBLE);
        holder.webView.setVisibility(View.INVISIBLE);

        // Cấu hình WebView
        WebSettings webSettings = holder.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        holder.webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                holder.videoProgressBar.setVisibility(View.GONE);
                holder.webView.setVisibility(View.VISIBLE);
            }
        });

        // Tải video YouTube qua iframe
        String videoId = extractYouTubeVideoId(video.getUrl()); // Đảm bảo chỉ chứa Video ID, không phải URL đầy đủ
        String iframeHtml = "<html><body style=\"margin:0;padding:0;\"><iframe width=\"100%\" height=\"100%\" " +
                "src=\"https://www.youtube.com/embed/" + videoId + "\" frameborder=\"0\" allowfullscreen></iframe></body></html>";

        holder.webView.loadData(iframeHtml, "text/html", "utf-8");
    }
    public String extractYouTubeVideoId(String youtubeUrl) {
        if (youtubeUrl == null || youtubeUrl.isEmpty()) {
            return null; // Kiểm tra nếu URL trống hoặc null
        }

        String videoId = null;

        try {
            // Trường hợp URL đầy đủ, ví dụ: https://www.youtube.com/watch?v=zILbsWPbyYnpQkVJ
            if (youtubeUrl.contains("v=")) {
                videoId = youtubeUrl.split("v=")[1]; // Lấy phần sau "v="
                int ampersandIndex = videoId.indexOf("&");
                if (ampersandIndex != -1) {
                    videoId = videoId.substring(0, ampersandIndex); // Bỏ các tham số sau "&"
                }
            }
            // Trường hợp URL dạng ngắn, ví dụ: https://youtu.be/zILbsWPbyYnpQkVJ
            else if (youtubeUrl.contains("youtu.be/")) {
                videoId = youtubeUrl.split("youtu.be/")[1];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return videoId; // Trả về video ID
    }



    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        WebView webView;
        ProgressBar videoProgressBar;
        TextView textVideoTitle, textVideoDescription;
        ImageView imPerson, favorites, imShare, imMore;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            webView = itemView.findViewById(R.id.webView);
            videoProgressBar = itemView.findViewById(R.id.videoProgressBar);
            textVideoTitle = itemView.findViewById(R.id.textVideoTitle);
            textVideoDescription = itemView.findViewById(R.id.textVideoDescription);
            imPerson = itemView.findViewById(R.id.imPerson);
            favorites = itemView.findViewById(R.id.favorites);
            imShare = itemView.findViewById(R.id.imShare);
            imMore = itemView.findViewById(R.id.imMore);
        }
    }
}
