/*
 * Copyright (c) 2019 WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.ballerinalang.stdlib.socket.tcp;

import java.util.HashMap;
import java.util.Map;

/**
 * This map will hold all the pending read action that coming through
 * {@link org.ballerinalang.stdlib.socket.endpoint.tcp.client.Read} action until new content ready.
 *
 * @since 0.995.0
 */
public class ReadPendingSocketMap {
    private Map<Integer, ReadPendingCallback> queue = new HashMap<>();
    private final Object lock = new Object();

    private ReadPendingSocketMap() {
    }

    private static class LazyHolder {
        private static final ReadPendingSocketMap INSTANCE = new ReadPendingSocketMap();
    }

    public static ReadPendingSocketMap getInstance() {
        return LazyHolder.INSTANCE;
    }

    public void add(Integer hashId, ReadPendingCallback readPendingCallback) {
        synchronized (lock) {
            queue.put(hashId, readPendingCallback);
        }
    }

    public ReadPendingCallback remove(int hashId) {
        synchronized (lock) {
            return queue.remove(hashId);
        }
    }

    public ReadPendingCallback get(int hashId) {
        return queue.get(hashId);
    }

    public boolean isPending(int hashId) {
        return queue.containsKey(hashId);
    }
}
