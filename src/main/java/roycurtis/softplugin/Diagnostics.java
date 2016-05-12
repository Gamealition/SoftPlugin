/**
 * Copyright © 2016 Roy Adrian Curtis
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the “Software”), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package roycurtis.softplugin;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.JavaFileObject;
import java.util.logging.Level;

import static roycurtis.softplugin.SoftPlugin.SOFTLOG;

/** Implementation of javac diagnostics handler. Reports warnings/errors to console */
public class Diagnostics implements DiagnosticListener<JavaFileObject>
{
    @Override
    public void report(Diagnostic<? extends JavaFileObject> event)
    {
        Level  level;
        String prefix;

        switch ( event.getKind() )
        {
            case ERROR:
                level  = Level.SEVERE;
                prefix = "ERROR";
                break;
            case WARNING:
            case MANDATORY_WARNING:
                level  = Level.WARNING;
                prefix = "WARN";
                break;
            default:
                level  = Level.FINE;
                prefix = "INFO";
        }

        SOFTLOG.log(level, "*** {0}: {1}", new Object[] {prefix, event.getMessage(null)} );

        if (event.getSource() != null)
            SOFTLOG.log(level, "* At {0} ({1}:{2})", new Object[] {
                event.getSource().getName(), event.getLineNumber(), event.getColumnNumber()
            });
    }
}
