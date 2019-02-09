package name.jervyshi.nacos.exception;

/**
 * The type Nacos embedded exception.
 * @author JervyShi
 * @version $Id : NacosEmbeddedException.java, v 0.1 2019-02-08 21:44 JervyShi Exp $$
 */
public class NacosEmbeddedException extends RuntimeException {

    /**
     * Instantiates a new Nacos embedded exception.
     */
    public NacosEmbeddedException() {
        super();
    }

    /**
     * Instantiates a new Nacos embedded exception.
     *
     * @param message the message
     */
    public NacosEmbeddedException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Nacos embedded exception.
     *
     * @param message the message 
     * @param cause the cause
     */
    public NacosEmbeddedException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new Nacos embedded exception.
     *
     * @param cause the cause
     */
    public NacosEmbeddedException(Throwable cause) {
        super(cause);
    }

    /**
     * Instantiates a new Nacos embedded exception.
     *
     * @param message the message 
     * @param cause the cause 
     * @param enableSuppression the enable suppression 
     * @param writableStackTrace the writable stack trace
     */
    protected NacosEmbeddedException(String message, Throwable cause, boolean enableSuppression,
                                     boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
