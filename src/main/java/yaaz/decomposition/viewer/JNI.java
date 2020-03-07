package yaaz.decomposition.viewer;


/**
 * Class responsible for JNI library loading.
 */
public class JNI {


    static {
        try {
            System.loadLibrary("decomposition_viewer_jni");
        } catch(UnsatisfiedLinkError e) {
            System.err.println("JNI library \"decomposition_viewer_jni\" not found, check your java.library.path");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Does nothing except causing class to be initialized.
     * Call this method to force JNI library loading if you don't like catching UnsatisfiedLinkErrors at runtime
     */
    public static void load() {}


}
