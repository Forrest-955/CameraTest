// IActionResponseListener.aidl
package com.itep.mt.common.ipc;

// Declare any non-default types here with import statements
import com.itep.mt.common.ipc.ActionResponse;

interface  IActionResponseListener {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    oneway void onResponse( in ActionResponse response );
}
