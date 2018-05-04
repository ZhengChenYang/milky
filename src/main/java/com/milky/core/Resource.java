package com.milky.core;

import java.io.InputStream;

/**
 * Created by 52678 on 2018/3/5.
 */
public interface Resource {

    InputStream getInputStream() throws Exception;
}
