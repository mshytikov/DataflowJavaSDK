/*
 * Copyright (C) 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.cloud.dataflow.sdk.transforms.windowing;

import org.joda.time.Instant;

import java.util.List;

/**
 * A trigger which never fires.
 *
 * <p>
 * Using this trigger will only produce output when the watermark passes the end of the
 * {@link BoundedWindow window} plus the {@link Window#withAllowedLateness() allowed
 * lateness}.
 */
public final class Never {
  /**
   * Returns a trigger which never fires. Output will be produced from the using {@link GroupByKey}
   * when the {@link BoundedWindow} closes.
   */
  public static Trigger.OnceTrigger ever() {
    // NeverTrigger ignores all inputs and is Window-type independent.
    return new NeverTrigger();
  }

  static class NeverTrigger<W extends BoundedWindow> extends Trigger.OnceTrigger<W> {
    protected NeverTrigger() {
      super(null);
    }

    @Override
    public void onElement(OnElementContext c) {}

    @Override
    public void onMerge(OnMergeContext c) {}

    @Override
    protected Trigger<W> getContinuationTrigger(List<Trigger<W>> continuationTriggers) {
      return this;
    }

    @Override
    public Instant getWatermarkThatGuaranteesFiring(BoundedWindow window) {
      return BoundedWindow.TIMESTAMP_MAX_VALUE;
    }

    @Override
    public boolean shouldFire(Trigger.TriggerContext context) {
      return false;
    }

    @Override
    protected void onOnlyFiring(Trigger.TriggerContext context) {
      throw new UnsupportedOperationException(
          String.format("%s should never fire", getClass().getSimpleName()));
    }
  }
}
