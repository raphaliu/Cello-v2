/*
 * Copyright (C) 2020 Boston University (BU)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.cellocad.v2.common.target.data.data;

import org.cellocad.v2.common.CObject;
import org.cellocad.v2.common.exception.CelloException;
import org.cellocad.v2.common.profile.ProfileUtils;
import org.json.simple.JSONObject;

/**
 * A base class representing an object that can be evaluated to obtain a numerical output.
 *
 * @author Timothy Jones
 * @date 2020-02-12
 */
public abstract class Evaluatable extends CObject {

  protected final void parseName(final JSONObject jsonObj) {
    final String value = ProfileUtils.getString(jsonObj, Evaluatable.S_NAME);
    setName(value);
  }

  public Evaluatable(final JSONObject jsonObj) {
    parseName(jsonObj);
  }

  public Evaluatable() {}

  @Override
  public boolean isValid() {
    boolean rtn = super.isValid();
    rtn = rtn && getName() != null;
    return rtn;
  }

  public abstract Number evaluate(EvaluationContext ec) throws CelloException;

  protected static final String S_NAME = "name";
}
