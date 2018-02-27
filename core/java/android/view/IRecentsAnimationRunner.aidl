/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.view;

import android.view.RemoteAnimationTarget;
import android.view.IRecentsAnimationController;

/**
 * Interface that is used to callback from window manager to the process that runs a recents
 * animation to start or cancel it.
 *
 * {@hide}
 */
oneway interface IRecentsAnimationRunner {

    /**
     * Called when the system is ready for the handler to start animating all the visible tasks.
     */
    void onAnimationStart(in IRecentsAnimationController controller,
            in RemoteAnimationTarget[] apps);

    /**
     * Called when the system needs to cancel the current animation. This can be due to the
     * wallpaper not drawing in time, or the handler not finishing the animation within a predefined
     * amount of time.
     */
    void onAnimationCanceled();
}
