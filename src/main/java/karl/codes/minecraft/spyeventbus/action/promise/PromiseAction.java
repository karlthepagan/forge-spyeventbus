package karl.codes.minecraft.spyeventbus.action.promise;

import com.google.common.base.Function;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import karl.codes.minecraft.spyeventbus.action.EventAction;
import karl.codes.minecraft.spyeventbus.action.EventActionBuilder;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.apache.commons.lang3.tuple.Triple;

import javax.annotation.Nullable;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by karl on 10/22/2015.
 */
public class PromiseAction extends EventActionBuilder<PromiseAction> {
    public static PromiseAction newChain() {
        return new PromiseAction();
    }

    /**
     *
     * @param value
     * @return
     */
    public PromiseAction intercept(Function<Triple<Event,ConcurrentMap<?,?>,EventAction.Result>,EventAction.Result> value) {
        return this;
    }

    /**
     *
     * @param value
     * @return
     */
    public PromiseAction then(Callable<Triple<Event,ConcurrentMap<?,?>,EventAction.Result>> value) {
        return this;
    }

    /**
     *
     * @param reason
     * @return
     */
    public PromiseAction fail(Throwable reason) {
        return this;
    }

    public Promise build() {
        return new Promise();
    }

    public static class Promise implements EventAction {
        @Nullable
        @Override
        public Result apply(Event event, ConcurrentMap<Object, Object> memory, Result last) {
            return null;
        }

        @Override
        public boolean creatememory() {
            return false;
        }
    }
}
