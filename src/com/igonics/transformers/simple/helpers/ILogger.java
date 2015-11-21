package com.igonics.transformers.simple.helpers;

/**
 * @author gggordon <https://github.com/gggordon>
 * @version 1.0.0
 * @created 1.11.2015
 *  
 * @description
 * Logging Interface
 * */
public interface ILogger {
    void info(Object message);
    void warn(Object message);
    void debug(Object message);
    void fatal(Object message);
    void fatal(Object message, Throwable e);
    void error(Object message);
    void error(Object message, Throwable e);
}
