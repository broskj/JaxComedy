package com.jacksonvillecomedy.broskj.jaxcomedy;

/**
 * Created by Kyle on 2/13/2015.
 *
 * From:
 * https://github.com/telerik/Android-samples/blob/master/Blogs/Json-Reader/app/src/main/java/com/example/progress/json_reader/AsyncResult.java
 */

import org.json.JSONObject;

/**
 * Created by kstanoev on 1/14/2015.
 */
interface AsyncResult
{
    void onResult(JSONObject object);
}
