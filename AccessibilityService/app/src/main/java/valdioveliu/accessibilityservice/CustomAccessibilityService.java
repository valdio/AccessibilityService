package valdioveliu.accessibilityservice;

import android.accessibilityservice.AccessibilityService;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by Valdio Veliu on 2/1/2016.
 */
public class CustomAccessibilityService extends AccessibilityService {

    private TextToSpeech textToSpeech;

    //Configure the Accessibility Service
    @Override
    protected void onServiceConnected() {
        Toast.makeText(getApplication(), "onServiceConnected", Toast.LENGTH_SHORT).show();

        //Init TextToSpeech
        textToSpeech = new TextToSpeech(getApplication(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TextToSpeech", "Language not supported");
                    }
                } else {
                    Log.e("TextToSpeech", "Initialization Failed! :( ");
                }
            }
        });
    }

    //Respond to AccessibilityEvents
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_CLICKED) {
            Toast.makeText(getApplication(), event.getText().toString(), Toast.LENGTH_SHORT).show();

            //TextToSpeech
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                textToSpeech.speak(event.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, "TextToSpeech_ID");
            } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                textToSpeech.speak(event.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
            }

        } else if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_SCROLLED) {

            //RecyclerView Scrolled
            //TextToSpeech
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                textToSpeech.speak("Scrolling", TextToSpeech.QUEUE_FLUSH, null, "TextToSpeech_ID");
            } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                textToSpeech.speak("Scrolling", TextToSpeech.QUEUE_FLUSH, null);
            }
        }

    }

    @Override
    public void onInterrupt() {
        //Interrupt the Accessibility service
        //Stop TextToSpeech
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }


}

