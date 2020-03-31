package io.reactivex;

import io.reactivex.annotations.BackpressureKind;
import io.reactivex.annotations.BackpressureSupport;
import io.reactivex.annotations.CheckReturnValue;
import io.reactivex.annotations.SchedulerSupport;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.BiPredicate;
import io.reactivex.functions.BooleanSupplier;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;
import io.reactivex.functions.Function4;
import io.reactivex.functions.Function5;
import io.reactivex.functions.Function6;
import io.reactivex.functions.Function7;
import io.reactivex.functions.Function8;
import io.reactivex.functions.Function9;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.functions.Functions;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.observers.BlockingFirstObserver;
import io.reactivex.internal.observers.BlockingLastObserver;
import io.reactivex.internal.observers.ForEachWhileObserver;
import io.reactivex.internal.observers.FutureObserver;
import io.reactivex.internal.observers.LambdaObserver;
import io.reactivex.internal.operators.flowable.FlowableFromObservable;
import io.reactivex.internal.operators.flowable.FlowableOnBackpressureError;
import io.reactivex.internal.operators.mixed.ObservableConcatMapCompletable;
import io.reactivex.internal.operators.mixed.ObservableConcatMapMaybe;
import io.reactivex.internal.operators.mixed.ObservableConcatMapSingle;
import io.reactivex.internal.operators.mixed.ObservableSwitchMapCompletable;
import io.reactivex.internal.operators.mixed.ObservableSwitchMapMaybe;
import io.reactivex.internal.operators.mixed.ObservableSwitchMapSingle;
import io.reactivex.internal.operators.observable.BlockingObservableIterable;
import io.reactivex.internal.operators.observable.BlockingObservableLatest;
import io.reactivex.internal.operators.observable.BlockingObservableMostRecent;
import io.reactivex.internal.operators.observable.BlockingObservableNext;
import io.reactivex.internal.operators.observable.ObservableAllSingle;
import io.reactivex.internal.operators.observable.ObservableAmb;
import io.reactivex.internal.operators.observable.ObservableAnySingle;
import io.reactivex.internal.operators.observable.ObservableBlockingSubscribe;
import io.reactivex.internal.operators.observable.ObservableBuffer;
import io.reactivex.internal.operators.observable.ObservableBufferBoundary;
import io.reactivex.internal.operators.observable.ObservableBufferBoundarySupplier;
import io.reactivex.internal.operators.observable.ObservableBufferExactBoundary;
import io.reactivex.internal.operators.observable.ObservableBufferTimed;
import io.reactivex.internal.operators.observable.ObservableCache;
import io.reactivex.internal.operators.observable.ObservableCollectSingle;
import io.reactivex.internal.operators.observable.ObservableCombineLatest;
import io.reactivex.internal.operators.observable.ObservableConcatMap;
import io.reactivex.internal.operators.observable.ObservableConcatMapEager;
import io.reactivex.internal.operators.observable.ObservableConcatWithCompletable;
import io.reactivex.internal.operators.observable.ObservableConcatWithMaybe;
import io.reactivex.internal.operators.observable.ObservableConcatWithSingle;
import io.reactivex.internal.operators.observable.ObservableCountSingle;
import io.reactivex.internal.operators.observable.ObservableCreate;
import io.reactivex.internal.operators.observable.ObservableDebounce;
import io.reactivex.internal.operators.observable.ObservableDebounceTimed;
import io.reactivex.internal.operators.observable.ObservableDefer;
import io.reactivex.internal.operators.observable.ObservableDelay;
import io.reactivex.internal.operators.observable.ObservableDelaySubscriptionOther;
import io.reactivex.internal.operators.observable.ObservableDematerialize;
import io.reactivex.internal.operators.observable.ObservableDetach;
import io.reactivex.internal.operators.observable.ObservableDistinct;
import io.reactivex.internal.operators.observable.ObservableDistinctUntilChanged;
import io.reactivex.internal.operators.observable.ObservableDoAfterNext;
import io.reactivex.internal.operators.observable.ObservableDoFinally;
import io.reactivex.internal.operators.observable.ObservableDoOnEach;
import io.reactivex.internal.operators.observable.ObservableDoOnLifecycle;
import io.reactivex.internal.operators.observable.ObservableElementAtMaybe;
import io.reactivex.internal.operators.observable.ObservableElementAtSingle;
import io.reactivex.internal.operators.observable.ObservableEmpty;
import io.reactivex.internal.operators.observable.ObservableError;
import io.reactivex.internal.operators.observable.ObservableFilter;
import io.reactivex.internal.operators.observable.ObservableFlatMap;
import io.reactivex.internal.operators.observable.ObservableFlatMapCompletableCompletable;
import io.reactivex.internal.operators.observable.ObservableFlatMapMaybe;
import io.reactivex.internal.operators.observable.ObservableFlatMapSingle;
import io.reactivex.internal.operators.observable.ObservableFlattenIterable;
import io.reactivex.internal.operators.observable.ObservableFromArray;
import io.reactivex.internal.operators.observable.ObservableFromCallable;
import io.reactivex.internal.operators.observable.ObservableFromFuture;
import io.reactivex.internal.operators.observable.ObservableFromIterable;
import io.reactivex.internal.operators.observable.ObservableFromPublisher;
import io.reactivex.internal.operators.observable.ObservableFromUnsafeSource;
import io.reactivex.internal.operators.observable.ObservableGenerate;
import io.reactivex.internal.operators.observable.ObservableGroupBy;
import io.reactivex.internal.operators.observable.ObservableGroupJoin;
import io.reactivex.internal.operators.observable.ObservableHide;
import io.reactivex.internal.operators.observable.ObservableIgnoreElements;
import io.reactivex.internal.operators.observable.ObservableIgnoreElementsCompletable;
import io.reactivex.internal.operators.observable.ObservableInternalHelper;
import io.reactivex.internal.operators.observable.ObservableInterval;
import io.reactivex.internal.operators.observable.ObservableIntervalRange;
import io.reactivex.internal.operators.observable.ObservableJoin;
import io.reactivex.internal.operators.observable.ObservableJust;
import io.reactivex.internal.operators.observable.ObservableLastMaybe;
import io.reactivex.internal.operators.observable.ObservableLastSingle;
import io.reactivex.internal.operators.observable.ObservableLift;
import io.reactivex.internal.operators.observable.ObservableMap;
import io.reactivex.internal.operators.observable.ObservableMapNotification;
import io.reactivex.internal.operators.observable.ObservableMaterialize;
import io.reactivex.internal.operators.observable.ObservableMergeWithCompletable;
import io.reactivex.internal.operators.observable.ObservableMergeWithMaybe;
import io.reactivex.internal.operators.observable.ObservableMergeWithSingle;
import io.reactivex.internal.operators.observable.ObservableNever;
import io.reactivex.internal.operators.observable.ObservableObserveOn;
import io.reactivex.internal.operators.observable.ObservableOnErrorNext;
import io.reactivex.internal.operators.observable.ObservableOnErrorReturn;
import io.reactivex.internal.operators.observable.ObservablePublish;
import io.reactivex.internal.operators.observable.ObservablePublishSelector;
import io.reactivex.internal.operators.observable.ObservableRange;
import io.reactivex.internal.operators.observable.ObservableRangeLong;
import io.reactivex.internal.operators.observable.ObservableReduceMaybe;
import io.reactivex.internal.operators.observable.ObservableReduceSeedSingle;
import io.reactivex.internal.operators.observable.ObservableReduceWithSingle;
import io.reactivex.internal.operators.observable.ObservableRepeat;
import io.reactivex.internal.operators.observable.ObservableRepeatUntil;
import io.reactivex.internal.operators.observable.ObservableRepeatWhen;
import io.reactivex.internal.operators.observable.ObservableReplay;
import io.reactivex.internal.operators.observable.ObservableRetryBiPredicate;
import io.reactivex.internal.operators.observable.ObservableRetryPredicate;
import io.reactivex.internal.operators.observable.ObservableRetryWhen;
import io.reactivex.internal.operators.observable.ObservableSampleTimed;
import io.reactivex.internal.operators.observable.ObservableSampleWithObservable;
import io.reactivex.internal.operators.observable.ObservableScan;
import io.reactivex.internal.operators.observable.ObservableScanSeed;
import io.reactivex.internal.operators.observable.ObservableSequenceEqualSingle;
import io.reactivex.internal.operators.observable.ObservableSerialized;
import io.reactivex.internal.operators.observable.ObservableSingleMaybe;
import io.reactivex.internal.operators.observable.ObservableSingleSingle;
import io.reactivex.internal.operators.observable.ObservableSkip;
import io.reactivex.internal.operators.observable.ObservableSkipLast;
import io.reactivex.internal.operators.observable.ObservableSkipLastTimed;
import io.reactivex.internal.operators.observable.ObservableSkipUntil;
import io.reactivex.internal.operators.observable.ObservableSkipWhile;
import io.reactivex.internal.operators.observable.ObservableSubscribeOn;
import io.reactivex.internal.operators.observable.ObservableSwitchIfEmpty;
import io.reactivex.internal.operators.observable.ObservableSwitchMap;
import io.reactivex.internal.operators.observable.ObservableTake;
import io.reactivex.internal.operators.observable.ObservableTakeLast;
import io.reactivex.internal.operators.observable.ObservableTakeLastOne;
import io.reactivex.internal.operators.observable.ObservableTakeLastTimed;
import io.reactivex.internal.operators.observable.ObservableTakeUntil;
import io.reactivex.internal.operators.observable.ObservableTakeUntilPredicate;
import io.reactivex.internal.operators.observable.ObservableTakeWhile;
import io.reactivex.internal.operators.observable.ObservableThrottleFirstTimed;
import io.reactivex.internal.operators.observable.ObservableThrottleLatest;
import io.reactivex.internal.operators.observable.ObservableTimeInterval;
import io.reactivex.internal.operators.observable.ObservableTimeout;
import io.reactivex.internal.operators.observable.ObservableTimeoutTimed;
import io.reactivex.internal.operators.observable.ObservableTimer;
import io.reactivex.internal.operators.observable.ObservableToList;
import io.reactivex.internal.operators.observable.ObservableToListSingle;
import io.reactivex.internal.operators.observable.ObservableUnsubscribeOn;
import io.reactivex.internal.operators.observable.ObservableUsing;
import io.reactivex.internal.operators.observable.ObservableWindow;
import io.reactivex.internal.operators.observable.ObservableWindowBoundary;
import io.reactivex.internal.operators.observable.ObservableWindowBoundarySelector;
import io.reactivex.internal.operators.observable.ObservableWindowBoundarySupplier;
import io.reactivex.internal.operators.observable.ObservableWindowTimed;
import io.reactivex.internal.operators.observable.ObservableWithLatestFrom;
import io.reactivex.internal.operators.observable.ObservableWithLatestFromMany;
import io.reactivex.internal.operators.observable.ObservableZip;
import io.reactivex.internal.operators.observable.ObservableZipIterable;
import io.reactivex.internal.util.ArrayListSupplier;
import io.reactivex.internal.util.ErrorMode;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.internal.util.HashMapSupplier;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.observables.GroupedObservable;
import io.reactivex.observers.SafeObserver;
import io.reactivex.observers.TestObserver;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.Timed;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;

public abstract class Observable<T> implements ObservableSource<T> {
    /* access modifiers changed from: protected */
    public abstract void subscribeActual(Observer<? super T> observer);

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> amb(Iterable<? extends ObservableSource<? extends T>> iterable) {
        ObjectHelper.requireNonNull(iterable, "sources is null");
        return RxJavaPlugins.onAssembly(new ObservableAmb(null, iterable));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> ambArray(ObservableSource<? extends T>... observableSourceArr) {
        ObjectHelper.requireNonNull(observableSourceArr, "sources is null");
        int length = observableSourceArr.length;
        if (length == 0) {
            return empty();
        }
        if (length == 1) {
            return wrap(observableSourceArr[0]);
        }
        return RxJavaPlugins.onAssembly(new ObservableAmb(observableSourceArr, null));
    }

    public static int bufferSize() {
        return Flowable.bufferSize();
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T, R> Observable<R> combineLatest(Function<? super Object[], ? extends R> function, int i, ObservableSource<? extends T>... observableSourceArr) {
        return combineLatest(observableSourceArr, function, i);
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [io.reactivex.functions.Function, io.reactivex.functions.Function<? super java.lang.Object[], ? extends R>] */
    /* JADX WARNING: Unknown variable types count: 1 */
    @io.reactivex.annotations.CheckReturnValue
    @io.reactivex.annotations.SchedulerSupport(io.reactivex.annotations.SchedulerSupport.NONE)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T, R> io.reactivex.Observable<R> combineLatest(java.lang.Iterable<? extends io.reactivex.ObservableSource<? extends T>> r1, io.reactivex.functions.Function<? super java.lang.Object[], ? extends R> r2) {
        /*
            int r0 = bufferSize()
            io.reactivex.Observable r1 = combineLatest(r1, r2, r0)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: io.reactivex.Observable.combineLatest(java.lang.Iterable, io.reactivex.functions.Function):io.reactivex.Observable");
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T, R> Observable<R> combineLatest(Iterable<? extends ObservableSource<? extends T>> iterable, Function<? super Object[], ? extends R> function, int i) {
        ObjectHelper.requireNonNull(iterable, "sources is null");
        ObjectHelper.requireNonNull(function, "combiner is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        return RxJavaPlugins.onAssembly(new ObservableCombineLatest(null, iterable, function, i << 1, false));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T, R> Observable<R> combineLatest(ObservableSource<? extends T>[] observableSourceArr, Function<? super Object[], ? extends R> function) {
        return combineLatest(observableSourceArr, function, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T, R> Observable<R> combineLatest(ObservableSource<? extends T>[] observableSourceArr, Function<? super Object[], ? extends R> function, int i) {
        ObjectHelper.requireNonNull(observableSourceArr, "sources is null");
        if (observableSourceArr.length == 0) {
            return empty();
        }
        ObjectHelper.requireNonNull(function, "combiner is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        return RxJavaPlugins.onAssembly(new ObservableCombineLatest(observableSourceArr, null, function, i << 1, false));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T1, T2, R> Observable<R> combineLatest(ObservableSource<? extends T1> observableSource, ObservableSource<? extends T2> observableSource2, BiFunction<? super T1, ? super T2, ? extends R> biFunction) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        return combineLatest(Functions.toFunction(biFunction), bufferSize(), observableSource, observableSource2);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T1, T2, T3, R> Observable<R> combineLatest(ObservableSource<? extends T1> observableSource, ObservableSource<? extends T2> observableSource2, ObservableSource<? extends T3> observableSource3, Function3<? super T1, ? super T2, ? super T3, ? extends R> function3) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        ObjectHelper.requireNonNull(observableSource3, "source3 is null");
        return combineLatest(Functions.toFunction(function3), bufferSize(), observableSource, observableSource2, observableSource3);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T1, T2, T3, T4, R> Observable<R> combineLatest(ObservableSource<? extends T1> observableSource, ObservableSource<? extends T2> observableSource2, ObservableSource<? extends T3> observableSource3, ObservableSource<? extends T4> observableSource4, Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> function4) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        ObjectHelper.requireNonNull(observableSource3, "source3 is null");
        ObjectHelper.requireNonNull(observableSource4, "source4 is null");
        return combineLatest(Functions.toFunction(function4), bufferSize(), observableSource, observableSource2, observableSource3, observableSource4);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T1, T2, T3, T4, T5, R> Observable<R> combineLatest(ObservableSource<? extends T1> observableSource, ObservableSource<? extends T2> observableSource2, ObservableSource<? extends T3> observableSource3, ObservableSource<? extends T4> observableSource4, ObservableSource<? extends T5> observableSource5, Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> function5) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        ObjectHelper.requireNonNull(observableSource3, "source3 is null");
        ObjectHelper.requireNonNull(observableSource4, "source4 is null");
        ObjectHelper.requireNonNull(observableSource5, "source5 is null");
        return combineLatest(Functions.toFunction(function5), bufferSize(), observableSource, observableSource2, observableSource3, observableSource4, observableSource5);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T1, T2, T3, T4, T5, T6, R> Observable<R> combineLatest(ObservableSource<? extends T1> observableSource, ObservableSource<? extends T2> observableSource2, ObservableSource<? extends T3> observableSource3, ObservableSource<? extends T4> observableSource4, ObservableSource<? extends T5> observableSource5, ObservableSource<? extends T6> observableSource6, Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> function6) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        ObjectHelper.requireNonNull(observableSource3, "source3 is null");
        ObjectHelper.requireNonNull(observableSource4, "source4 is null");
        ObjectHelper.requireNonNull(observableSource5, "source5 is null");
        ObjectHelper.requireNonNull(observableSource6, "source6 is null");
        return combineLatest(Functions.toFunction(function6), bufferSize(), observableSource, observableSource2, observableSource3, observableSource4, observableSource5, observableSource6);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T1, T2, T3, T4, T5, T6, T7, R> Observable<R> combineLatest(ObservableSource<? extends T1> observableSource, ObservableSource<? extends T2> observableSource2, ObservableSource<? extends T3> observableSource3, ObservableSource<? extends T4> observableSource4, ObservableSource<? extends T5> observableSource5, ObservableSource<? extends T6> observableSource6, ObservableSource<? extends T7> observableSource7, Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> function7) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        ObjectHelper.requireNonNull(observableSource3, "source3 is null");
        ObjectHelper.requireNonNull(observableSource4, "source4 is null");
        ObjectHelper.requireNonNull(observableSource5, "source5 is null");
        ObjectHelper.requireNonNull(observableSource6, "source6 is null");
        ObjectHelper.requireNonNull(observableSource7, "source7 is null");
        return combineLatest(Functions.toFunction(function7), bufferSize(), observableSource, observableSource2, observableSource3, observableSource4, observableSource5, observableSource6, observableSource7);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T1, T2, T3, T4, T5, T6, T7, T8, R> Observable<R> combineLatest(ObservableSource<? extends T1> observableSource, ObservableSource<? extends T2> observableSource2, ObservableSource<? extends T3> observableSource3, ObservableSource<? extends T4> observableSource4, ObservableSource<? extends T5> observableSource5, ObservableSource<? extends T6> observableSource6, ObservableSource<? extends T7> observableSource7, ObservableSource<? extends T8> observableSource8, Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> function8) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        ObjectHelper.requireNonNull(observableSource3, "source3 is null");
        ObjectHelper.requireNonNull(observableSource4, "source4 is null");
        ObjectHelper.requireNonNull(observableSource5, "source5 is null");
        ObjectHelper.requireNonNull(observableSource6, "source6 is null");
        ObjectHelper.requireNonNull(observableSource7, "source7 is null");
        ObjectHelper.requireNonNull(observableSource8, "source8 is null");
        return combineLatest(Functions.toFunction(function8), bufferSize(), observableSource, observableSource2, observableSource3, observableSource4, observableSource5, observableSource6, observableSource7, observableSource8);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Observable<R> combineLatest(ObservableSource<? extends T1> observableSource, ObservableSource<? extends T2> observableSource2, ObservableSource<? extends T3> observableSource3, ObservableSource<? extends T4> observableSource4, ObservableSource<? extends T5> observableSource5, ObservableSource<? extends T6> observableSource6, ObservableSource<? extends T7> observableSource7, ObservableSource<? extends T8> observableSource8, ObservableSource<? extends T9> observableSource9, Function9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? extends R> function9) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        ObjectHelper.requireNonNull(observableSource3, "source3 is null");
        ObjectHelper.requireNonNull(observableSource4, "source4 is null");
        ObjectHelper.requireNonNull(observableSource5, "source5 is null");
        ObjectHelper.requireNonNull(observableSource6, "source6 is null");
        ObjectHelper.requireNonNull(observableSource7, "source7 is null");
        ObjectHelper.requireNonNull(observableSource8, "source8 is null");
        ObjectHelper.requireNonNull(observableSource9, "source9 is null");
        return combineLatest(Functions.toFunction(function9), bufferSize(), observableSource, observableSource2, observableSource3, observableSource4, observableSource5, observableSource6, observableSource7, observableSource8, observableSource9);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T, R> Observable<R> combineLatestDelayError(ObservableSource<? extends T>[] observableSourceArr, Function<? super Object[], ? extends R> function) {
        return combineLatestDelayError(observableSourceArr, function, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T, R> Observable<R> combineLatestDelayError(Function<? super Object[], ? extends R> function, int i, ObservableSource<? extends T>... observableSourceArr) {
        return combineLatestDelayError(observableSourceArr, function, i);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T, R> Observable<R> combineLatestDelayError(ObservableSource<? extends T>[] observableSourceArr, Function<? super Object[], ? extends R> function, int i) {
        ObjectHelper.verifyPositive(i, "bufferSize");
        ObjectHelper.requireNonNull(function, "combiner is null");
        if (observableSourceArr.length == 0) {
            return empty();
        }
        return RxJavaPlugins.onAssembly(new ObservableCombineLatest(observableSourceArr, null, function, i << 1, true));
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [io.reactivex.functions.Function, io.reactivex.functions.Function<? super java.lang.Object[], ? extends R>] */
    /* JADX WARNING: Unknown variable types count: 1 */
    @io.reactivex.annotations.CheckReturnValue
    @io.reactivex.annotations.SchedulerSupport(io.reactivex.annotations.SchedulerSupport.NONE)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T, R> io.reactivex.Observable<R> combineLatestDelayError(java.lang.Iterable<? extends io.reactivex.ObservableSource<? extends T>> r1, io.reactivex.functions.Function<? super java.lang.Object[], ? extends R> r2) {
        /*
            int r0 = bufferSize()
            io.reactivex.Observable r1 = combineLatestDelayError(r1, r2, r0)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: io.reactivex.Observable.combineLatestDelayError(java.lang.Iterable, io.reactivex.functions.Function):io.reactivex.Observable");
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T, R> Observable<R> combineLatestDelayError(Iterable<? extends ObservableSource<? extends T>> iterable, Function<? super Object[], ? extends R> function, int i) {
        ObjectHelper.requireNonNull(iterable, "sources is null");
        ObjectHelper.requireNonNull(function, "combiner is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        return RxJavaPlugins.onAssembly(new ObservableCombineLatest(null, iterable, function, i << 1, true));
    }

    /* JADX WARN: Type inference failed for: r3v0, types: [java.lang.Iterable<? extends io.reactivex.ObservableSource<? extends T>>, java.lang.Object, java.lang.Iterable] */
    /* JADX WARNING: Unknown variable types count: 1 */
    @io.reactivex.annotations.CheckReturnValue
    @io.reactivex.annotations.SchedulerSupport(io.reactivex.annotations.SchedulerSupport.NONE)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> io.reactivex.Observable<T> concat(java.lang.Iterable<? extends io.reactivex.ObservableSource<? extends T>> r3) {
        /*
            java.lang.String r0 = "sources is null"
            io.reactivex.internal.functions.ObjectHelper.requireNonNull(r3, r0)
            io.reactivex.Observable r3 = fromIterable(r3)
            io.reactivex.functions.Function r0 = io.reactivex.internal.functions.Functions.identity()
            int r1 = bufferSize()
            r2 = 0
            io.reactivex.Observable r3 = r3.concatMapDelayError(r0, r1, r2)
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: io.reactivex.Observable.concat(java.lang.Iterable):io.reactivex.Observable");
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> concat(ObservableSource<? extends ObservableSource<? extends T>> observableSource) {
        return concat(observableSource, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> concat(ObservableSource<? extends ObservableSource<? extends T>> observableSource, int i) {
        ObjectHelper.requireNonNull(observableSource, "sources is null");
        ObjectHelper.verifyPositive(i, "prefetch");
        return RxJavaPlugins.onAssembly(new ObservableConcatMap(observableSource, Functions.identity(), i, ErrorMode.IMMEDIATE));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> concat(ObservableSource<? extends T> observableSource, ObservableSource<? extends T> observableSource2) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        return concatArray(observableSource, observableSource2);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> concat(ObservableSource<? extends T> observableSource, ObservableSource<? extends T> observableSource2, ObservableSource<? extends T> observableSource3) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        ObjectHelper.requireNonNull(observableSource3, "source3 is null");
        return concatArray(observableSource, observableSource2, observableSource3);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> concat(ObservableSource<? extends T> observableSource, ObservableSource<? extends T> observableSource2, ObservableSource<? extends T> observableSource3, ObservableSource<? extends T> observableSource4) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        ObjectHelper.requireNonNull(observableSource3, "source3 is null");
        ObjectHelper.requireNonNull(observableSource4, "source4 is null");
        return concatArray(observableSource, observableSource2, observableSource3, observableSource4);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> concatArray(ObservableSource<? extends T>... observableSourceArr) {
        if (observableSourceArr.length == 0) {
            return empty();
        }
        if (observableSourceArr.length == 1) {
            return wrap(observableSourceArr[0]);
        }
        return RxJavaPlugins.onAssembly(new ObservableConcatMap(fromArray(observableSourceArr), Functions.identity(), bufferSize(), ErrorMode.BOUNDARY));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> concatArrayDelayError(ObservableSource<? extends T>... observableSourceArr) {
        if (observableSourceArr.length == 0) {
            return empty();
        }
        if (observableSourceArr.length == 1) {
            return wrap(observableSourceArr[0]);
        }
        return concatDelayError(fromArray(observableSourceArr));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> concatArrayEager(ObservableSource<? extends T>... observableSourceArr) {
        return concatArrayEager(bufferSize(), bufferSize(), observableSourceArr);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> concatArrayEager(int i, int i2, ObservableSource<? extends T>... observableSourceArr) {
        return fromArray(observableSourceArr).concatMapEagerDelayError(Functions.identity(), i, i2, false);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> concatArrayEagerDelayError(ObservableSource<? extends T>... observableSourceArr) {
        return concatArrayEagerDelayError(bufferSize(), bufferSize(), observableSourceArr);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> concatArrayEagerDelayError(int i, int i2, ObservableSource<? extends T>... observableSourceArr) {
        return fromArray(observableSourceArr).concatMapEagerDelayError(Functions.identity(), i, i2, true);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> concatDelayError(Iterable<? extends ObservableSource<? extends T>> iterable) {
        ObjectHelper.requireNonNull(iterable, "sources is null");
        return concatDelayError(fromIterable(iterable));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> concatDelayError(ObservableSource<? extends ObservableSource<? extends T>> observableSource) {
        return concatDelayError(observableSource, bufferSize(), true);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> concatDelayError(ObservableSource<? extends ObservableSource<? extends T>> observableSource, int i, boolean z) {
        ObjectHelper.requireNonNull(observableSource, "sources is null");
        ObjectHelper.verifyPositive(i, "prefetch is null");
        return RxJavaPlugins.onAssembly(new ObservableConcatMap(observableSource, Functions.identity(), i, z ? ErrorMode.END : ErrorMode.BOUNDARY));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> concatEager(ObservableSource<? extends ObservableSource<? extends T>> observableSource) {
        return concatEager(observableSource, bufferSize(), bufferSize());
    }

    /* JADX WARN: Type inference failed for: r1v0, types: [io.reactivex.ObservableSource<? extends io.reactivex.ObservableSource<? extends T>>, io.reactivex.ObservableSource] */
    /* JADX WARNING: Unknown variable types count: 1 */
    @io.reactivex.annotations.CheckReturnValue
    @io.reactivex.annotations.SchedulerSupport(io.reactivex.annotations.SchedulerSupport.NONE)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> io.reactivex.Observable<T> concatEager(io.reactivex.ObservableSource<? extends io.reactivex.ObservableSource<? extends T>> r1, int r2, int r3) {
        /*
            io.reactivex.Observable r1 = wrap(r1)
            io.reactivex.functions.Function r0 = io.reactivex.internal.functions.Functions.identity()
            io.reactivex.Observable r1 = r1.concatMapEager(r0, r2, r3)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: io.reactivex.Observable.concatEager(io.reactivex.ObservableSource, int, int):io.reactivex.Observable");
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> concatEager(Iterable<? extends ObservableSource<? extends T>> iterable) {
        return concatEager(iterable, bufferSize(), bufferSize());
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [java.lang.Iterable<? extends io.reactivex.ObservableSource<? extends T>>, java.lang.Iterable] */
    /* JADX WARNING: Unknown variable types count: 1 */
    @io.reactivex.annotations.CheckReturnValue
    @io.reactivex.annotations.SchedulerSupport(io.reactivex.annotations.SchedulerSupport.NONE)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> io.reactivex.Observable<T> concatEager(java.lang.Iterable<? extends io.reactivex.ObservableSource<? extends T>> r2, int r3, int r4) {
        /*
            io.reactivex.Observable r2 = fromIterable(r2)
            io.reactivex.functions.Function r0 = io.reactivex.internal.functions.Functions.identity()
            r1 = 0
            io.reactivex.Observable r2 = r2.concatMapEagerDelayError(r0, r3, r4, r1)
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: io.reactivex.Observable.concatEager(java.lang.Iterable, int, int):io.reactivex.Observable");
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> create(ObservableOnSubscribe<T> observableOnSubscribe) {
        ObjectHelper.requireNonNull(observableOnSubscribe, "source is null");
        return RxJavaPlugins.onAssembly(new ObservableCreate(observableOnSubscribe));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> defer(Callable<? extends ObservableSource<? extends T>> callable) {
        ObjectHelper.requireNonNull(callable, "supplier is null");
        return RxJavaPlugins.onAssembly(new ObservableDefer(callable));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> empty() {
        return RxJavaPlugins.onAssembly(ObservableEmpty.INSTANCE);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> error(Callable<? extends Throwable> callable) {
        ObjectHelper.requireNonNull(callable, "errorSupplier is null");
        return RxJavaPlugins.onAssembly(new ObservableError(callable));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> error(Throwable th) {
        ObjectHelper.requireNonNull(th, "exception is null");
        return error(Functions.justCallable(th));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> fromArray(T... tArr) {
        ObjectHelper.requireNonNull(tArr, "items is null");
        if (tArr.length == 0) {
            return empty();
        }
        if (tArr.length == 1) {
            return just(tArr[0]);
        }
        return RxJavaPlugins.onAssembly(new ObservableFromArray(tArr));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> fromCallable(Callable<? extends T> callable) {
        ObjectHelper.requireNonNull(callable, "supplier is null");
        return RxJavaPlugins.onAssembly(new ObservableFromCallable(callable));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> fromFuture(Future<? extends T> future) {
        ObjectHelper.requireNonNull(future, "future is null");
        return RxJavaPlugins.onAssembly(new ObservableFromFuture(future, 0, null));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> fromFuture(Future<? extends T> future, long j, TimeUnit timeUnit) {
        ObjectHelper.requireNonNull(future, "future is null");
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        return RxJavaPlugins.onAssembly(new ObservableFromFuture(future, j, timeUnit));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public static <T> Observable<T> fromFuture(Future<? extends T> future, long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return fromFuture(future, j, timeUnit).subscribeOn(scheduler);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public static <T> Observable<T> fromFuture(Future<? extends T> future, Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return fromFuture(future).subscribeOn(scheduler);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> fromIterable(Iterable<? extends T> iterable) {
        ObjectHelper.requireNonNull(iterable, "source is null");
        return RxJavaPlugins.onAssembly(new ObservableFromIterable(iterable));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> fromPublisher(Publisher<? extends T> publisher) {
        ObjectHelper.requireNonNull(publisher, "publisher is null");
        return RxJavaPlugins.onAssembly(new ObservableFromPublisher(publisher));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> generate(Consumer<Emitter<T>> consumer) {
        ObjectHelper.requireNonNull(consumer, "generator is null");
        return generate(Functions.nullSupplier(), ObservableInternalHelper.simpleGenerator(consumer), Functions.emptyConsumer());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T, S> Observable<T> generate(Callable<S> callable, BiConsumer<S, Emitter<T>> biConsumer) {
        ObjectHelper.requireNonNull(biConsumer, "generator is null");
        return generate(callable, ObservableInternalHelper.simpleBiGenerator(biConsumer), Functions.emptyConsumer());
    }

    /* JADX WARN: Type inference failed for: r3v0, types: [io.reactivex.functions.Consumer<? super S>, io.reactivex.functions.Consumer] */
    /* JADX WARNING: Unknown variable types count: 1 */
    @io.reactivex.annotations.CheckReturnValue
    @io.reactivex.annotations.SchedulerSupport(io.reactivex.annotations.SchedulerSupport.NONE)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T, S> io.reactivex.Observable<T> generate(java.util.concurrent.Callable<S> r1, io.reactivex.functions.BiConsumer<S, io.reactivex.Emitter<T>> r2, io.reactivex.functions.Consumer<? super S> r3) {
        /*
            java.lang.String r0 = "generator is null"
            io.reactivex.internal.functions.ObjectHelper.requireNonNull(r2, r0)
            io.reactivex.functions.BiFunction r2 = io.reactivex.internal.operators.observable.ObservableInternalHelper.simpleBiGenerator(r2)
            io.reactivex.Observable r1 = generate(r1, r2, r3)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: io.reactivex.Observable.generate(java.util.concurrent.Callable, io.reactivex.functions.BiConsumer, io.reactivex.functions.Consumer):io.reactivex.Observable");
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T, S> Observable<T> generate(Callable<S> callable, BiFunction<S, Emitter<T>, S> biFunction) {
        return generate(callable, biFunction, Functions.emptyConsumer());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T, S> Observable<T> generate(Callable<S> callable, BiFunction<S, Emitter<T>, S> biFunction, Consumer<? super S> consumer) {
        ObjectHelper.requireNonNull(callable, "initialState is null");
        ObjectHelper.requireNonNull(biFunction, "generator is null");
        ObjectHelper.requireNonNull(consumer, "disposeState is null");
        return RxJavaPlugins.onAssembly(new ObservableGenerate(callable, biFunction, consumer));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.COMPUTATION)
    public static Observable<Long> interval(long j, long j2, TimeUnit timeUnit) {
        return interval(j, j2, timeUnit, Schedulers.computation());
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.lang.Math.max(long, long):long}
     arg types: [int, long]
     candidates:
      ClspMth{java.lang.Math.max(double, double):double}
      ClspMth{java.lang.Math.max(int, int):int}
      ClspMth{java.lang.Math.max(float, float):float}
      ClspMth{java.lang.Math.max(long, long):long} */
    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public static Observable<Long> interval(long j, long j2, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new ObservableInterval(Math.max(0L, j), Math.max(0L, j2), timeUnit, scheduler));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.COMPUTATION)
    public static Observable<Long> interval(long j, TimeUnit timeUnit) {
        return interval(j, j, timeUnit, Schedulers.computation());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public static Observable<Long> interval(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return interval(j, j, timeUnit, scheduler);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.COMPUTATION)
    public static Observable<Long> intervalRange(long j, long j2, long j3, long j4, TimeUnit timeUnit) {
        return intervalRange(j, j2, j3, j4, timeUnit, Schedulers.computation());
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.lang.Math.max(long, long):long}
     arg types: [int, long]
     candidates:
      ClspMth{java.lang.Math.max(double, double):double}
      ClspMth{java.lang.Math.max(int, int):int}
      ClspMth{java.lang.Math.max(float, float):float}
      ClspMth{java.lang.Math.max(long, long):long} */
    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public static Observable<Long> intervalRange(long j, long j2, long j3, long j4, TimeUnit timeUnit, Scheduler scheduler) {
        long j5 = j2;
        long j6 = j3;
        TimeUnit timeUnit2 = timeUnit;
        Scheduler scheduler2 = scheduler;
        int i = (j5 > 0 ? 1 : (j5 == 0 ? 0 : -1));
        if (i < 0) {
            throw new IllegalArgumentException("count >= 0 required but it was " + j5);
        } else if (i == 0) {
            return empty().delay(j6, timeUnit2, scheduler2);
        } else {
            long j7 = j + (j5 - 1);
            if (j <= 0 || j7 >= 0) {
                ObjectHelper.requireNonNull(timeUnit2, "unit is null");
                ObjectHelper.requireNonNull(scheduler2, "scheduler is null");
                return RxJavaPlugins.onAssembly(new ObservableIntervalRange(j, j7, Math.max(0L, j6), Math.max(0L, j4), timeUnit, scheduler));
            }
            throw new IllegalArgumentException("Overflow! start + count is bigger than Long.MAX_VALUE");
        }
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> just(T t) {
        ObjectHelper.requireNonNull(t, "item is null");
        return RxJavaPlugins.onAssembly(new ObservableJust(t));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> just(T t, T t2) {
        ObjectHelper.requireNonNull(t, "item1 is null");
        ObjectHelper.requireNonNull(t2, "item2 is null");
        return fromArray(t, t2);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> just(T t, T t2, T t3) {
        ObjectHelper.requireNonNull(t, "item1 is null");
        ObjectHelper.requireNonNull(t2, "item2 is null");
        ObjectHelper.requireNonNull(t3, "item3 is null");
        return fromArray(t, t2, t3);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> just(T t, T t2, T t3, T t4) {
        ObjectHelper.requireNonNull(t, "item1 is null");
        ObjectHelper.requireNonNull(t2, "item2 is null");
        ObjectHelper.requireNonNull(t3, "item3 is null");
        ObjectHelper.requireNonNull(t4, "item4 is null");
        return fromArray(t, t2, t3, t4);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> just(T t, T t2, T t3, T t4, T t5) {
        ObjectHelper.requireNonNull(t, "item1 is null");
        ObjectHelper.requireNonNull(t2, "item2 is null");
        ObjectHelper.requireNonNull(t3, "item3 is null");
        ObjectHelper.requireNonNull(t4, "item4 is null");
        ObjectHelper.requireNonNull(t5, "item5 is null");
        return fromArray(t, t2, t3, t4, t5);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> just(T t, T t2, T t3, T t4, T t5, T t6) {
        ObjectHelper.requireNonNull(t, "item1 is null");
        ObjectHelper.requireNonNull(t2, "item2 is null");
        ObjectHelper.requireNonNull(t3, "item3 is null");
        ObjectHelper.requireNonNull(t4, "item4 is null");
        ObjectHelper.requireNonNull(t5, "item5 is null");
        ObjectHelper.requireNonNull(t6, "item6 is null");
        return fromArray(t, t2, t3, t4, t5, t6);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> just(T t, T t2, T t3, T t4, T t5, T t6, T t7) {
        ObjectHelper.requireNonNull(t, "item1 is null");
        ObjectHelper.requireNonNull(t2, "item2 is null");
        ObjectHelper.requireNonNull(t3, "item3 is null");
        ObjectHelper.requireNonNull(t4, "item4 is null");
        ObjectHelper.requireNonNull(t5, "item5 is null");
        ObjectHelper.requireNonNull(t6, "item6 is null");
        ObjectHelper.requireNonNull(t7, "item7 is null");
        return fromArray(t, t2, t3, t4, t5, t6, t7);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> just(T t, T t2, T t3, T t4, T t5, T t6, T t7, T t8) {
        ObjectHelper.requireNonNull(t, "item1 is null");
        ObjectHelper.requireNonNull(t2, "item2 is null");
        ObjectHelper.requireNonNull(t3, "item3 is null");
        ObjectHelper.requireNonNull(t4, "item4 is null");
        ObjectHelper.requireNonNull(t5, "item5 is null");
        ObjectHelper.requireNonNull(t6, "item6 is null");
        ObjectHelper.requireNonNull(t7, "item7 is null");
        ObjectHelper.requireNonNull(t8, "item8 is null");
        return fromArray(t, t2, t3, t4, t5, t6, t7, t8);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> just(T t, T t2, T t3, T t4, T t5, T t6, T t7, T t8, T t9) {
        ObjectHelper.requireNonNull(t, "item1 is null");
        ObjectHelper.requireNonNull(t2, "item2 is null");
        ObjectHelper.requireNonNull(t3, "item3 is null");
        ObjectHelper.requireNonNull(t4, "item4 is null");
        ObjectHelper.requireNonNull(t5, "item5 is null");
        ObjectHelper.requireNonNull(t6, "item6 is null");
        ObjectHelper.requireNonNull(t7, "item7 is null");
        ObjectHelper.requireNonNull(t8, "item8 is null");
        ObjectHelper.requireNonNull(t9, "item9 is null");
        return fromArray(t, t2, t3, t4, t5, t6, t7, t8, t9);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> just(T t, T t2, T t3, T t4, T t5, T t6, T t7, T t8, T t9, T t10) {
        ObjectHelper.requireNonNull(t, "item1 is null");
        ObjectHelper.requireNonNull(t2, "item2 is null");
        ObjectHelper.requireNonNull(t3, "item3 is null");
        ObjectHelper.requireNonNull(t4, "item4 is null");
        ObjectHelper.requireNonNull(t5, "item5 is null");
        ObjectHelper.requireNonNull(t6, "item6 is null");
        ObjectHelper.requireNonNull(t7, "item7 is null");
        ObjectHelper.requireNonNull(t8, "item8 is null");
        ObjectHelper.requireNonNull(t9, "item9 is null");
        ObjectHelper.requireNonNull(t10, "item10 is null");
        return fromArray(t, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [java.lang.Iterable<? extends io.reactivex.ObservableSource<? extends T>>, java.lang.Iterable] */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: io.reactivex.Observable.flatMap(io.reactivex.functions.Function, boolean, int, int):io.reactivex.Observable<R>
     arg types: [io.reactivex.functions.Function, int, int, int]
     candidates:
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, io.reactivex.functions.BiFunction, boolean, int):io.reactivex.Observable<R>
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, io.reactivex.functions.Function, java.util.concurrent.Callable, int):io.reactivex.Observable<R>
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, boolean, int, int):io.reactivex.Observable<R> */
    /* JADX WARNING: Unknown variable types count: 1 */
    @io.reactivex.annotations.CheckReturnValue
    @io.reactivex.annotations.SchedulerSupport(io.reactivex.annotations.SchedulerSupport.NONE)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> io.reactivex.Observable<T> merge(java.lang.Iterable<? extends io.reactivex.ObservableSource<? extends T>> r2, int r3, int r4) {
        /*
            io.reactivex.Observable r2 = fromIterable(r2)
            io.reactivex.functions.Function r0 = io.reactivex.internal.functions.Functions.identity()
            r1 = 0
            io.reactivex.Observable r2 = r2.flatMap(r0, r1, r3, r4)
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: io.reactivex.Observable.merge(java.lang.Iterable, int, int):io.reactivex.Observable");
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: io.reactivex.Observable.flatMap(io.reactivex.functions.Function, boolean, int, int):io.reactivex.Observable<R>
     arg types: [io.reactivex.functions.Function, int, int, int]
     candidates:
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, io.reactivex.functions.BiFunction, boolean, int):io.reactivex.Observable<R>
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, io.reactivex.functions.Function, java.util.concurrent.Callable, int):io.reactivex.Observable<R>
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, boolean, int, int):io.reactivex.Observable<R> */
    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> mergeArray(int i, int i2, ObservableSource<? extends T>... observableSourceArr) {
        return fromArray(observableSourceArr).flatMap(Functions.identity(), false, i, i2);
    }

    /* JADX WARN: Type inference failed for: r1v0, types: [java.lang.Iterable<? extends io.reactivex.ObservableSource<? extends T>>, java.lang.Iterable] */
    /* JADX WARNING: Unknown variable types count: 1 */
    @io.reactivex.annotations.CheckReturnValue
    @io.reactivex.annotations.SchedulerSupport(io.reactivex.annotations.SchedulerSupport.NONE)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> io.reactivex.Observable<T> merge(java.lang.Iterable<? extends io.reactivex.ObservableSource<? extends T>> r1) {
        /*
            io.reactivex.Observable r1 = fromIterable(r1)
            io.reactivex.functions.Function r0 = io.reactivex.internal.functions.Functions.identity()
            io.reactivex.Observable r1 = r1.flatMap(r0)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: io.reactivex.Observable.merge(java.lang.Iterable):io.reactivex.Observable");
    }

    /* JADX WARN: Type inference failed for: r1v0, types: [java.lang.Iterable<? extends io.reactivex.ObservableSource<? extends T>>, java.lang.Iterable] */
    /* JADX WARNING: Unknown variable types count: 1 */
    @io.reactivex.annotations.CheckReturnValue
    @io.reactivex.annotations.SchedulerSupport(io.reactivex.annotations.SchedulerSupport.NONE)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> io.reactivex.Observable<T> merge(java.lang.Iterable<? extends io.reactivex.ObservableSource<? extends T>> r1, int r2) {
        /*
            io.reactivex.Observable r1 = fromIterable(r1)
            io.reactivex.functions.Function r0 = io.reactivex.internal.functions.Functions.identity()
            io.reactivex.Observable r1 = r1.flatMap(r0, r2)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: io.reactivex.Observable.merge(java.lang.Iterable, int):io.reactivex.Observable");
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> merge(ObservableSource<? extends ObservableSource<? extends T>> observableSource) {
        ObjectHelper.requireNonNull(observableSource, "sources is null");
        return RxJavaPlugins.onAssembly(new ObservableFlatMap(observableSource, Functions.identity(), false, Integer.MAX_VALUE, bufferSize()));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> merge(ObservableSource<? extends ObservableSource<? extends T>> observableSource, int i) {
        ObjectHelper.requireNonNull(observableSource, "sources is null");
        ObjectHelper.verifyPositive(i, "maxConcurrency");
        return RxJavaPlugins.onAssembly(new ObservableFlatMap(observableSource, Functions.identity(), false, i, bufferSize()));
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: io.reactivex.Observable.flatMap(io.reactivex.functions.Function, boolean, int):io.reactivex.Observable<R>
     arg types: [io.reactivex.functions.Function, int, int]
     candidates:
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, io.reactivex.functions.BiFunction, int):io.reactivex.Observable<R>
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, io.reactivex.functions.BiFunction, boolean):io.reactivex.Observable<R>
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, io.reactivex.functions.Function, java.util.concurrent.Callable):io.reactivex.Observable<R>
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, boolean, int):io.reactivex.Observable<R> */
    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> merge(ObservableSource<? extends T> observableSource, ObservableSource<? extends T> observableSource2) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        return fromArray(observableSource, observableSource2).flatMap(Functions.identity(), false, 2);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: io.reactivex.Observable.flatMap(io.reactivex.functions.Function, boolean, int):io.reactivex.Observable<R>
     arg types: [io.reactivex.functions.Function, int, int]
     candidates:
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, io.reactivex.functions.BiFunction, int):io.reactivex.Observable<R>
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, io.reactivex.functions.BiFunction, boolean):io.reactivex.Observable<R>
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, io.reactivex.functions.Function, java.util.concurrent.Callable):io.reactivex.Observable<R>
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, boolean, int):io.reactivex.Observable<R> */
    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> merge(ObservableSource<? extends T> observableSource, ObservableSource<? extends T> observableSource2, ObservableSource<? extends T> observableSource3) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        ObjectHelper.requireNonNull(observableSource3, "source3 is null");
        return fromArray(observableSource, observableSource2, observableSource3).flatMap(Functions.identity(), false, 3);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: io.reactivex.Observable.flatMap(io.reactivex.functions.Function, boolean, int):io.reactivex.Observable<R>
     arg types: [io.reactivex.functions.Function, int, int]
     candidates:
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, io.reactivex.functions.BiFunction, int):io.reactivex.Observable<R>
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, io.reactivex.functions.BiFunction, boolean):io.reactivex.Observable<R>
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, io.reactivex.functions.Function, java.util.concurrent.Callable):io.reactivex.Observable<R>
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, boolean, int):io.reactivex.Observable<R> */
    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> merge(ObservableSource<? extends T> observableSource, ObservableSource<? extends T> observableSource2, ObservableSource<? extends T> observableSource3, ObservableSource<? extends T> observableSource4) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        ObjectHelper.requireNonNull(observableSource3, "source3 is null");
        ObjectHelper.requireNonNull(observableSource4, "source4 is null");
        return fromArray(observableSource, observableSource2, observableSource3, observableSource4).flatMap(Functions.identity(), false, 4);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> mergeArray(ObservableSource<? extends T>... observableSourceArr) {
        return fromArray(observableSourceArr).flatMap(Functions.identity(), observableSourceArr.length);
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [java.lang.Iterable<? extends io.reactivex.ObservableSource<? extends T>>, java.lang.Iterable] */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: io.reactivex.Observable.flatMap(io.reactivex.functions.Function, boolean):io.reactivex.Observable<R>
     arg types: [io.reactivex.functions.Function, int]
     candidates:
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, int):io.reactivex.Observable<R>
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, io.reactivex.functions.BiFunction):io.reactivex.Observable<R>
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, boolean):io.reactivex.Observable<R> */
    /* JADX WARNING: Unknown variable types count: 1 */
    @io.reactivex.annotations.CheckReturnValue
    @io.reactivex.annotations.SchedulerSupport(io.reactivex.annotations.SchedulerSupport.NONE)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> io.reactivex.Observable<T> mergeDelayError(java.lang.Iterable<? extends io.reactivex.ObservableSource<? extends T>> r2) {
        /*
            io.reactivex.Observable r2 = fromIterable(r2)
            io.reactivex.functions.Function r0 = io.reactivex.internal.functions.Functions.identity()
            r1 = 1
            io.reactivex.Observable r2 = r2.flatMap(r0, r1)
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: io.reactivex.Observable.mergeDelayError(java.lang.Iterable):io.reactivex.Observable");
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [java.lang.Iterable<? extends io.reactivex.ObservableSource<? extends T>>, java.lang.Iterable] */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: io.reactivex.Observable.flatMap(io.reactivex.functions.Function, boolean, int, int):io.reactivex.Observable<R>
     arg types: [io.reactivex.functions.Function, int, int, int]
     candidates:
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, io.reactivex.functions.BiFunction, boolean, int):io.reactivex.Observable<R>
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, io.reactivex.functions.Function, java.util.concurrent.Callable, int):io.reactivex.Observable<R>
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, boolean, int, int):io.reactivex.Observable<R> */
    /* JADX WARNING: Unknown variable types count: 1 */
    @io.reactivex.annotations.CheckReturnValue
    @io.reactivex.annotations.SchedulerSupport(io.reactivex.annotations.SchedulerSupport.NONE)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> io.reactivex.Observable<T> mergeDelayError(java.lang.Iterable<? extends io.reactivex.ObservableSource<? extends T>> r2, int r3, int r4) {
        /*
            io.reactivex.Observable r2 = fromIterable(r2)
            io.reactivex.functions.Function r0 = io.reactivex.internal.functions.Functions.identity()
            r1 = 1
            io.reactivex.Observable r2 = r2.flatMap(r0, r1, r3, r4)
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: io.reactivex.Observable.mergeDelayError(java.lang.Iterable, int, int):io.reactivex.Observable");
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: io.reactivex.Observable.flatMap(io.reactivex.functions.Function, boolean, int, int):io.reactivex.Observable<R>
     arg types: [io.reactivex.functions.Function, int, int, int]
     candidates:
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, io.reactivex.functions.BiFunction, boolean, int):io.reactivex.Observable<R>
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, io.reactivex.functions.Function, java.util.concurrent.Callable, int):io.reactivex.Observable<R>
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, boolean, int, int):io.reactivex.Observable<R> */
    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> mergeArrayDelayError(int i, int i2, ObservableSource<? extends T>... observableSourceArr) {
        return fromArray(observableSourceArr).flatMap(Functions.identity(), true, i, i2);
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [java.lang.Iterable<? extends io.reactivex.ObservableSource<? extends T>>, java.lang.Iterable] */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: io.reactivex.Observable.flatMap(io.reactivex.functions.Function, boolean, int):io.reactivex.Observable<R>
     arg types: [io.reactivex.functions.Function, int, int]
     candidates:
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, io.reactivex.functions.BiFunction, int):io.reactivex.Observable<R>
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, io.reactivex.functions.BiFunction, boolean):io.reactivex.Observable<R>
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, io.reactivex.functions.Function, java.util.concurrent.Callable):io.reactivex.Observable<R>
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, boolean, int):io.reactivex.Observable<R> */
    /* JADX WARNING: Unknown variable types count: 1 */
    @io.reactivex.annotations.CheckReturnValue
    @io.reactivex.annotations.SchedulerSupport(io.reactivex.annotations.SchedulerSupport.NONE)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> io.reactivex.Observable<T> mergeDelayError(java.lang.Iterable<? extends io.reactivex.ObservableSource<? extends T>> r2, int r3) {
        /*
            io.reactivex.Observable r2 = fromIterable(r2)
            io.reactivex.functions.Function r0 = io.reactivex.internal.functions.Functions.identity()
            r1 = 1
            io.reactivex.Observable r2 = r2.flatMap(r0, r1, r3)
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: io.reactivex.Observable.mergeDelayError(java.lang.Iterable, int):io.reactivex.Observable");
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> mergeDelayError(ObservableSource<? extends ObservableSource<? extends T>> observableSource) {
        ObjectHelper.requireNonNull(observableSource, "sources is null");
        return RxJavaPlugins.onAssembly(new ObservableFlatMap(observableSource, Functions.identity(), true, Integer.MAX_VALUE, bufferSize()));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> mergeDelayError(ObservableSource<? extends ObservableSource<? extends T>> observableSource, int i) {
        ObjectHelper.requireNonNull(observableSource, "sources is null");
        ObjectHelper.verifyPositive(i, "maxConcurrency");
        return RxJavaPlugins.onAssembly(new ObservableFlatMap(observableSource, Functions.identity(), true, i, bufferSize()));
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: io.reactivex.Observable.flatMap(io.reactivex.functions.Function, boolean, int):io.reactivex.Observable<R>
     arg types: [io.reactivex.functions.Function, int, int]
     candidates:
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, io.reactivex.functions.BiFunction, int):io.reactivex.Observable<R>
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, io.reactivex.functions.BiFunction, boolean):io.reactivex.Observable<R>
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, io.reactivex.functions.Function, java.util.concurrent.Callable):io.reactivex.Observable<R>
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, boolean, int):io.reactivex.Observable<R> */
    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> mergeDelayError(ObservableSource<? extends T> observableSource, ObservableSource<? extends T> observableSource2) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        return fromArray(observableSource, observableSource2).flatMap(Functions.identity(), true, 2);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: io.reactivex.Observable.flatMap(io.reactivex.functions.Function, boolean, int):io.reactivex.Observable<R>
     arg types: [io.reactivex.functions.Function, int, int]
     candidates:
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, io.reactivex.functions.BiFunction, int):io.reactivex.Observable<R>
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, io.reactivex.functions.BiFunction, boolean):io.reactivex.Observable<R>
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, io.reactivex.functions.Function, java.util.concurrent.Callable):io.reactivex.Observable<R>
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, boolean, int):io.reactivex.Observable<R> */
    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> mergeDelayError(ObservableSource<? extends T> observableSource, ObservableSource<? extends T> observableSource2, ObservableSource<? extends T> observableSource3) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        ObjectHelper.requireNonNull(observableSource3, "source3 is null");
        return fromArray(observableSource, observableSource2, observableSource3).flatMap(Functions.identity(), true, 3);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: io.reactivex.Observable.flatMap(io.reactivex.functions.Function, boolean, int):io.reactivex.Observable<R>
     arg types: [io.reactivex.functions.Function, int, int]
     candidates:
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, io.reactivex.functions.BiFunction, int):io.reactivex.Observable<R>
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, io.reactivex.functions.BiFunction, boolean):io.reactivex.Observable<R>
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, io.reactivex.functions.Function, java.util.concurrent.Callable):io.reactivex.Observable<R>
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, boolean, int):io.reactivex.Observable<R> */
    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> mergeDelayError(ObservableSource<? extends T> observableSource, ObservableSource<? extends T> observableSource2, ObservableSource<? extends T> observableSource3, ObservableSource<? extends T> observableSource4) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        ObjectHelper.requireNonNull(observableSource3, "source3 is null");
        ObjectHelper.requireNonNull(observableSource4, "source4 is null");
        return fromArray(observableSource, observableSource2, observableSource3, observableSource4).flatMap(Functions.identity(), true, 4);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: io.reactivex.Observable.flatMap(io.reactivex.functions.Function, boolean, int):io.reactivex.Observable<R>
     arg types: [io.reactivex.functions.Function, int, int]
     candidates:
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, io.reactivex.functions.BiFunction, int):io.reactivex.Observable<R>
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, io.reactivex.functions.BiFunction, boolean):io.reactivex.Observable<R>
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, io.reactivex.functions.Function, java.util.concurrent.Callable):io.reactivex.Observable<R>
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, boolean, int):io.reactivex.Observable<R> */
    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> mergeArrayDelayError(ObservableSource<? extends T>... observableSourceArr) {
        return fromArray(observableSourceArr).flatMap(Functions.identity(), true, observableSourceArr.length);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> never() {
        return RxJavaPlugins.onAssembly(ObservableNever.INSTANCE);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static Observable<Integer> range(int i, int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException("count >= 0 required but it was " + i2);
        } else if (i2 == 0) {
            return empty();
        } else {
            if (i2 == 1) {
                return just(Integer.valueOf(i));
            }
            if (((long) i) + ((long) (i2 - 1)) <= 2147483647L) {
                return RxJavaPlugins.onAssembly(new ObservableRange(i, i2));
            }
            throw new IllegalArgumentException("Integer overflow");
        }
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static Observable<Long> rangeLong(long j, long j2) {
        int i = (j2 > 0 ? 1 : (j2 == 0 ? 0 : -1));
        if (i < 0) {
            throw new IllegalArgumentException("count >= 0 required but it was " + j2);
        } else if (i == 0) {
            return empty();
        } else {
            if (j2 == 1) {
                return just(Long.valueOf(j));
            }
            long j3 = (j2 - 1) + j;
            if (j <= 0 || j3 >= 0) {
                return RxJavaPlugins.onAssembly(new ObservableRangeLong(j, j2));
            }
            throw new IllegalArgumentException("Overflow! start + count is bigger than Long.MAX_VALUE");
        }
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Single<Boolean> sequenceEqual(ObservableSource<? extends T> observableSource, ObservableSource<? extends T> observableSource2) {
        return sequenceEqual(observableSource, observableSource2, ObjectHelper.equalsPredicate(), bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Single<Boolean> sequenceEqual(ObservableSource<? extends T> observableSource, ObservableSource<? extends T> observableSource2, BiPredicate<? super T, ? super T> biPredicate) {
        return sequenceEqual(observableSource, observableSource2, biPredicate, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Single<Boolean> sequenceEqual(ObservableSource<? extends T> observableSource, ObservableSource<? extends T> observableSource2, BiPredicate<? super T, ? super T> biPredicate, int i) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        ObjectHelper.requireNonNull(biPredicate, "isEqual is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        return RxJavaPlugins.onAssembly(new ObservableSequenceEqualSingle(observableSource, observableSource2, biPredicate, i));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Single<Boolean> sequenceEqual(ObservableSource<? extends T> observableSource, ObservableSource<? extends T> observableSource2, int i) {
        return sequenceEqual(observableSource, observableSource2, ObjectHelper.equalsPredicate(), i);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> switchOnNext(ObservableSource<? extends ObservableSource<? extends T>> observableSource, int i) {
        ObjectHelper.requireNonNull(observableSource, "sources is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        return RxJavaPlugins.onAssembly(new ObservableSwitchMap(observableSource, Functions.identity(), i, false));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> switchOnNext(ObservableSource<? extends ObservableSource<? extends T>> observableSource) {
        return switchOnNext(observableSource, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> switchOnNextDelayError(ObservableSource<? extends ObservableSource<? extends T>> observableSource) {
        return switchOnNextDelayError(observableSource, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> switchOnNextDelayError(ObservableSource<? extends ObservableSource<? extends T>> observableSource, int i) {
        ObjectHelper.requireNonNull(observableSource, "sources is null");
        ObjectHelper.verifyPositive(i, "prefetch");
        return RxJavaPlugins.onAssembly(new ObservableSwitchMap(observableSource, Functions.identity(), i, true));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.COMPUTATION)
    public static Observable<Long> timer(long j, TimeUnit timeUnit) {
        return timer(j, timeUnit, Schedulers.computation());
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.lang.Math.max(long, long):long}
     arg types: [long, int]
     candidates:
      ClspMth{java.lang.Math.max(double, double):double}
      ClspMth{java.lang.Math.max(int, int):int}
      ClspMth{java.lang.Math.max(float, float):float}
      ClspMth{java.lang.Math.max(long, long):long} */
    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public static Observable<Long> timer(long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new ObservableTimer(Math.max(j, 0L), timeUnit, scheduler));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> unsafeCreate(ObservableSource<T> observableSource) {
        ObjectHelper.requireNonNull(observableSource, "onSubscribe is null");
        if (!(observableSource instanceof Observable)) {
            return RxJavaPlugins.onAssembly(new ObservableFromUnsafeSource(observableSource));
        }
        throw new IllegalArgumentException("unsafeCreate(Observable) should be upgraded");
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [io.reactivex.functions.Function, io.reactivex.functions.Function<? super D, ? extends io.reactivex.ObservableSource<? extends T>>] */
    /* JADX WARN: Type inference failed for: r3v0, types: [io.reactivex.functions.Consumer<? super D>, io.reactivex.functions.Consumer] */
    /* JADX WARNING: Unknown variable types count: 2 */
    @io.reactivex.annotations.CheckReturnValue
    @io.reactivex.annotations.SchedulerSupport(io.reactivex.annotations.SchedulerSupport.NONE)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T, D> io.reactivex.Observable<T> using(java.util.concurrent.Callable<? extends D> r1, io.reactivex.functions.Function<? super D, ? extends io.reactivex.ObservableSource<? extends T>> r2, io.reactivex.functions.Consumer<? super D> r3) {
        /*
            r0 = 1
            io.reactivex.Observable r1 = using(r1, r2, r3, r0)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: io.reactivex.Observable.using(java.util.concurrent.Callable, io.reactivex.functions.Function, io.reactivex.functions.Consumer):io.reactivex.Observable");
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T, D> Observable<T> using(Callable<? extends D> callable, Function<? super D, ? extends ObservableSource<? extends T>> function, Consumer<? super D> consumer, boolean z) {
        ObjectHelper.requireNonNull(callable, "resourceSupplier is null");
        ObjectHelper.requireNonNull(function, "sourceSupplier is null");
        ObjectHelper.requireNonNull(consumer, "disposer is null");
        return RxJavaPlugins.onAssembly(new ObservableUsing(callable, function, consumer, z));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> wrap(ObservableSource<T> observableSource) {
        ObjectHelper.requireNonNull(observableSource, "source is null");
        if (observableSource instanceof Observable) {
            return RxJavaPlugins.onAssembly((Observable) observableSource);
        }
        return RxJavaPlugins.onAssembly(new ObservableFromUnsafeSource(observableSource));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T, R> Observable<R> zip(Iterable<? extends ObservableSource<? extends T>> iterable, Function<? super Object[], ? extends R> function) {
        ObjectHelper.requireNonNull(function, "zipper is null");
        ObjectHelper.requireNonNull(iterable, "sources is null");
        return RxJavaPlugins.onAssembly(new ObservableZip(null, iterable, function, bufferSize(), false));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T, R> Observable<R> zip(ObservableSource<? extends ObservableSource<? extends T>> observableSource, Function<? super Object[], ? extends R> function) {
        ObjectHelper.requireNonNull(function, "zipper is null");
        ObjectHelper.requireNonNull(observableSource, "sources is null");
        return RxJavaPlugins.onAssembly(new ObservableToList(observableSource, 16).flatMap(ObservableInternalHelper.zipIterable(function)));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T1, T2, R> Observable<R> zip(ObservableSource<? extends T1> observableSource, ObservableSource<? extends T2> observableSource2, BiFunction<? super T1, ? super T2, ? extends R> biFunction) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        return zipArray(Functions.toFunction(biFunction), false, bufferSize(), observableSource, observableSource2);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T1, T2, R> Observable<R> zip(ObservableSource<? extends T1> observableSource, ObservableSource<? extends T2> observableSource2, BiFunction<? super T1, ? super T2, ? extends R> biFunction, boolean z) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        return zipArray(Functions.toFunction(biFunction), z, bufferSize(), observableSource, observableSource2);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T1, T2, R> Observable<R> zip(ObservableSource<? extends T1> observableSource, ObservableSource<? extends T2> observableSource2, BiFunction<? super T1, ? super T2, ? extends R> biFunction, boolean z, int i) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        return zipArray(Functions.toFunction(biFunction), z, i, observableSource, observableSource2);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T1, T2, T3, R> Observable<R> zip(ObservableSource<? extends T1> observableSource, ObservableSource<? extends T2> observableSource2, ObservableSource<? extends T3> observableSource3, Function3<? super T1, ? super T2, ? super T3, ? extends R> function3) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        ObjectHelper.requireNonNull(observableSource3, "source3 is null");
        return zipArray(Functions.toFunction(function3), false, bufferSize(), observableSource, observableSource2, observableSource3);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T1, T2, T3, T4, R> Observable<R> zip(ObservableSource<? extends T1> observableSource, ObservableSource<? extends T2> observableSource2, ObservableSource<? extends T3> observableSource3, ObservableSource<? extends T4> observableSource4, Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> function4) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        ObjectHelper.requireNonNull(observableSource3, "source3 is null");
        ObjectHelper.requireNonNull(observableSource4, "source4 is null");
        return zipArray(Functions.toFunction(function4), false, bufferSize(), observableSource, observableSource2, observableSource3, observableSource4);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T1, T2, T3, T4, T5, R> Observable<R> zip(ObservableSource<? extends T1> observableSource, ObservableSource<? extends T2> observableSource2, ObservableSource<? extends T3> observableSource3, ObservableSource<? extends T4> observableSource4, ObservableSource<? extends T5> observableSource5, Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> function5) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        ObjectHelper.requireNonNull(observableSource3, "source3 is null");
        ObjectHelper.requireNonNull(observableSource4, "source4 is null");
        ObjectHelper.requireNonNull(observableSource5, "source5 is null");
        return zipArray(Functions.toFunction(function5), false, bufferSize(), observableSource, observableSource2, observableSource3, observableSource4, observableSource5);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T1, T2, T3, T4, T5, T6, R> Observable<R> zip(ObservableSource<? extends T1> observableSource, ObservableSource<? extends T2> observableSource2, ObservableSource<? extends T3> observableSource3, ObservableSource<? extends T4> observableSource4, ObservableSource<? extends T5> observableSource5, ObservableSource<? extends T6> observableSource6, Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> function6) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        ObjectHelper.requireNonNull(observableSource3, "source3 is null");
        ObjectHelper.requireNonNull(observableSource4, "source4 is null");
        ObjectHelper.requireNonNull(observableSource5, "source5 is null");
        ObjectHelper.requireNonNull(observableSource6, "source6 is null");
        return zipArray(Functions.toFunction(function6), false, bufferSize(), observableSource, observableSource2, observableSource3, observableSource4, observableSource5, observableSource6);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T1, T2, T3, T4, T5, T6, T7, R> Observable<R> zip(ObservableSource<? extends T1> observableSource, ObservableSource<? extends T2> observableSource2, ObservableSource<? extends T3> observableSource3, ObservableSource<? extends T4> observableSource4, ObservableSource<? extends T5> observableSource5, ObservableSource<? extends T6> observableSource6, ObservableSource<? extends T7> observableSource7, Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> function7) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        ObjectHelper.requireNonNull(observableSource3, "source3 is null");
        ObjectHelper.requireNonNull(observableSource4, "source4 is null");
        ObjectHelper.requireNonNull(observableSource5, "source5 is null");
        ObjectHelper.requireNonNull(observableSource6, "source6 is null");
        ObjectHelper.requireNonNull(observableSource7, "source7 is null");
        return zipArray(Functions.toFunction(function7), false, bufferSize(), observableSource, observableSource2, observableSource3, observableSource4, observableSource5, observableSource6, observableSource7);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T1, T2, T3, T4, T5, T6, T7, T8, R> Observable<R> zip(ObservableSource<? extends T1> observableSource, ObservableSource<? extends T2> observableSource2, ObservableSource<? extends T3> observableSource3, ObservableSource<? extends T4> observableSource4, ObservableSource<? extends T5> observableSource5, ObservableSource<? extends T6> observableSource6, ObservableSource<? extends T7> observableSource7, ObservableSource<? extends T8> observableSource8, Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> function8) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        ObjectHelper.requireNonNull(observableSource3, "source3 is null");
        ObjectHelper.requireNonNull(observableSource4, "source4 is null");
        ObjectHelper.requireNonNull(observableSource5, "source5 is null");
        ObjectHelper.requireNonNull(observableSource6, "source6 is null");
        ObjectHelper.requireNonNull(observableSource7, "source7 is null");
        ObjectHelper.requireNonNull(observableSource8, "source8 is null");
        return zipArray(Functions.toFunction(function8), false, bufferSize(), observableSource, observableSource2, observableSource3, observableSource4, observableSource5, observableSource6, observableSource7, observableSource8);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Observable<R> zip(ObservableSource<? extends T1> observableSource, ObservableSource<? extends T2> observableSource2, ObservableSource<? extends T3> observableSource3, ObservableSource<? extends T4> observableSource4, ObservableSource<? extends T5> observableSource5, ObservableSource<? extends T6> observableSource6, ObservableSource<? extends T7> observableSource7, ObservableSource<? extends T8> observableSource8, ObservableSource<? extends T9> observableSource9, Function9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? extends R> function9) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        ObjectHelper.requireNonNull(observableSource3, "source3 is null");
        ObjectHelper.requireNonNull(observableSource4, "source4 is null");
        ObjectHelper.requireNonNull(observableSource5, "source5 is null");
        ObjectHelper.requireNonNull(observableSource6, "source6 is null");
        ObjectHelper.requireNonNull(observableSource7, "source7 is null");
        ObjectHelper.requireNonNull(observableSource8, "source8 is null");
        ObjectHelper.requireNonNull(observableSource9, "source9 is null");
        return zipArray(Functions.toFunction(function9), false, bufferSize(), observableSource, observableSource2, observableSource3, observableSource4, observableSource5, observableSource6, observableSource7, observableSource8, observableSource9);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T, R> Observable<R> zipArray(Function<? super Object[], ? extends R> function, boolean z, int i, ObservableSource<? extends T>... observableSourceArr) {
        if (observableSourceArr.length == 0) {
            return empty();
        }
        ObjectHelper.requireNonNull(function, "zipper is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        return RxJavaPlugins.onAssembly(new ObservableZip(observableSourceArr, null, function, i, z));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T, R> Observable<R> zipIterable(Iterable<? extends ObservableSource<? extends T>> iterable, Function<? super Object[], ? extends R> function, boolean z, int i) {
        ObjectHelper.requireNonNull(function, "zipper is null");
        ObjectHelper.requireNonNull(iterable, "sources is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        return RxJavaPlugins.onAssembly(new ObservableZip(null, iterable, function, i, z));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Single<Boolean> all(Predicate<? super T> predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly(new ObservableAllSingle(this, predicate));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> ambWith(ObservableSource<? extends T> observableSource) {
        ObjectHelper.requireNonNull(observableSource, "other is null");
        return ambArray(this, observableSource);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Single<Boolean> any(Predicate<? super T> predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly(new ObservableAnySingle(this, predicate));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> R as(ObservableConverter<T, ? extends R> observableConverter) {
        return ((ObservableConverter) ObjectHelper.requireNonNull(observableConverter, "converter is null")).apply(this);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final T blockingFirst() {
        BlockingFirstObserver blockingFirstObserver = new BlockingFirstObserver();
        subscribe(blockingFirstObserver);
        T blockingGet = blockingFirstObserver.blockingGet();
        if (blockingGet != null) {
            return blockingGet;
        }
        throw new NoSuchElementException();
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final T blockingFirst(T t) {
        BlockingFirstObserver blockingFirstObserver = new BlockingFirstObserver();
        subscribe(blockingFirstObserver);
        T blockingGet = blockingFirstObserver.blockingGet();
        return blockingGet != null ? blockingGet : t;
    }

    @SchedulerSupport(SchedulerSupport.NONE)
    public final void blockingForEach(Consumer<? super T> consumer) {
        Iterator it = blockingIterable().iterator();
        while (it.hasNext()) {
            try {
                consumer.accept(it.next());
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                ((Disposable) it).dispose();
                throw ExceptionHelper.wrapOrThrow(th);
            }
        }
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Iterable<T> blockingIterable() {
        return blockingIterable(bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Iterable<T> blockingIterable(int i) {
        ObjectHelper.verifyPositive(i, "bufferSize");
        return new BlockingObservableIterable(this, i);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final T blockingLast() {
        BlockingLastObserver blockingLastObserver = new BlockingLastObserver();
        subscribe(blockingLastObserver);
        T blockingGet = blockingLastObserver.blockingGet();
        if (blockingGet != null) {
            return blockingGet;
        }
        throw new NoSuchElementException();
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final T blockingLast(T t) {
        BlockingLastObserver blockingLastObserver = new BlockingLastObserver();
        subscribe(blockingLastObserver);
        T blockingGet = blockingLastObserver.blockingGet();
        return blockingGet != null ? blockingGet : t;
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Iterable<T> blockingLatest() {
        return new BlockingObservableLatest(this);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Iterable<T> blockingMostRecent(T t) {
        return new BlockingObservableMostRecent(this, t);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Iterable<T> blockingNext() {
        return new BlockingObservableNext(this);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final T blockingSingle() {
        T blockingGet = singleElement().blockingGet();
        if (blockingGet != null) {
            return blockingGet;
        }
        throw new NoSuchElementException();
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final T blockingSingle(T t) {
        return single(t).blockingGet();
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Future<T> toFuture() {
        return (Future) subscribeWith(new FutureObserver());
    }

    @SchedulerSupport(SchedulerSupport.NONE)
    public final void blockingSubscribe() {
        ObservableBlockingSubscribe.subscribe(this);
    }

    @SchedulerSupport(SchedulerSupport.NONE)
    public final void blockingSubscribe(Consumer<? super T> consumer) {
        ObservableBlockingSubscribe.subscribe(this, consumer, Functions.ON_ERROR_MISSING, Functions.EMPTY_ACTION);
    }

    @SchedulerSupport(SchedulerSupport.NONE)
    public final void blockingSubscribe(Consumer<? super T> consumer, Consumer<? super Throwable> consumer2) {
        ObservableBlockingSubscribe.subscribe(this, consumer, consumer2, Functions.EMPTY_ACTION);
    }

    @SchedulerSupport(SchedulerSupport.NONE)
    public final void blockingSubscribe(Consumer<? super T> consumer, Consumer<? super Throwable> consumer2, Action action) {
        ObservableBlockingSubscribe.subscribe(this, consumer, consumer2, action);
    }

    @SchedulerSupport(SchedulerSupport.NONE)
    public final void blockingSubscribe(Observer<? super T> observer) {
        ObservableBlockingSubscribe.subscribe(this, observer);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<List<T>> buffer(int i) {
        return buffer(i, i);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<List<T>> buffer(int i, int i2) {
        return buffer(i, i2, ArrayListSupplier.asCallable());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <U extends Collection<? super T>> Observable<U> buffer(int i, int i2, Callable<U> callable) {
        ObjectHelper.verifyPositive(i, "count");
        ObjectHelper.verifyPositive(i2, "skip");
        ObjectHelper.requireNonNull(callable, "bufferSupplier is null");
        return RxJavaPlugins.onAssembly(new ObservableBuffer(this, i, i2, callable));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <U extends Collection<? super T>> Observable<U> buffer(int i, Callable<U> callable) {
        return buffer(i, i, callable);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.COMPUTATION)
    public final Observable<List<T>> buffer(long j, long j2, TimeUnit timeUnit) {
        return buffer(j, j2, timeUnit, Schedulers.computation(), ArrayListSupplier.asCallable());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final Observable<List<T>> buffer(long j, long j2, TimeUnit timeUnit, Scheduler scheduler) {
        return buffer(j, j2, timeUnit, scheduler, ArrayListSupplier.asCallable());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final <U extends Collection<? super T>> Observable<U> buffer(long j, long j2, TimeUnit timeUnit, Scheduler scheduler, Callable<U> callable) {
        TimeUnit timeUnit2 = timeUnit;
        ObjectHelper.requireNonNull(timeUnit2, "unit is null");
        Scheduler scheduler2 = scheduler;
        ObjectHelper.requireNonNull(scheduler2, "scheduler is null");
        Callable<U> callable2 = callable;
        ObjectHelper.requireNonNull(callable2, "bufferSupplier is null");
        return RxJavaPlugins.onAssembly(new ObservableBufferTimed(this, j, j2, timeUnit2, scheduler2, callable2, Integer.MAX_VALUE, false));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.COMPUTATION)
    public final Observable<List<T>> buffer(long j, TimeUnit timeUnit) {
        return buffer(j, timeUnit, Schedulers.computation(), Integer.MAX_VALUE);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.COMPUTATION)
    public final Observable<List<T>> buffer(long j, TimeUnit timeUnit, int i) {
        return buffer(j, timeUnit, Schedulers.computation(), i);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final Observable<List<T>> buffer(long j, TimeUnit timeUnit, Scheduler scheduler, int i) {
        return buffer(j, timeUnit, scheduler, i, ArrayListSupplier.asCallable(), false);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final <U extends Collection<? super T>> Observable<U> buffer(long j, TimeUnit timeUnit, Scheduler scheduler, int i, Callable<U> callable, boolean z) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        Scheduler scheduler2 = scheduler;
        ObjectHelper.requireNonNull(scheduler2, "scheduler is null");
        Callable<U> callable2 = callable;
        ObjectHelper.requireNonNull(callable2, "bufferSupplier is null");
        int i2 = i;
        ObjectHelper.verifyPositive(i2, "count");
        return RxJavaPlugins.onAssembly(new ObservableBufferTimed(this, j, j, timeUnit, scheduler2, callable2, i2, z));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final Observable<List<T>> buffer(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return buffer(j, timeUnit, scheduler, Integer.MAX_VALUE, ArrayListSupplier.asCallable(), false);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <TOpening, TClosing> Observable<List<T>> buffer(ObservableSource<? extends TOpening> observableSource, Function<? super TOpening, ? extends ObservableSource<? extends TClosing>> function) {
        return buffer(observableSource, function, ArrayListSupplier.asCallable());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <TOpening, TClosing, U extends Collection<? super T>> Observable<U> buffer(ObservableSource<? extends TOpening> observableSource, Function<? super TOpening, ? extends ObservableSource<? extends TClosing>> function, Callable<U> callable) {
        ObjectHelper.requireNonNull(observableSource, "openingIndicator is null");
        ObjectHelper.requireNonNull(function, "closingIndicator is null");
        ObjectHelper.requireNonNull(callable, "bufferSupplier is null");
        return RxJavaPlugins.onAssembly(new ObservableBufferBoundary(this, observableSource, function, callable));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <B> Observable<List<T>> buffer(ObservableSource<B> observableSource) {
        return buffer(observableSource, ArrayListSupplier.asCallable());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <B> Observable<List<T>> buffer(ObservableSource<B> observableSource, int i) {
        ObjectHelper.verifyPositive(i, "initialCapacity");
        return buffer(observableSource, Functions.createArrayList(i));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <B, U extends Collection<? super T>> Observable<U> buffer(ObservableSource<B> observableSource, Callable<U> callable) {
        ObjectHelper.requireNonNull(observableSource, "boundary is null");
        ObjectHelper.requireNonNull(callable, "bufferSupplier is null");
        return RxJavaPlugins.onAssembly(new ObservableBufferExactBoundary(this, observableSource, callable));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <B> Observable<List<T>> buffer(Callable<? extends ObservableSource<B>> callable) {
        return buffer(callable, ArrayListSupplier.asCallable());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <B, U extends Collection<? super T>> Observable<U> buffer(Callable<? extends ObservableSource<B>> callable, Callable<U> callable2) {
        ObjectHelper.requireNonNull(callable, "boundarySupplier is null");
        ObjectHelper.requireNonNull(callable2, "bufferSupplier is null");
        return RxJavaPlugins.onAssembly(new ObservableBufferBoundarySupplier(this, callable, callable2));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> cache() {
        return cacheWithInitialCapacity(16);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> cacheWithInitialCapacity(int i) {
        ObjectHelper.verifyPositive(i, "initialCapacity");
        return RxJavaPlugins.onAssembly(new ObservableCache(this, i));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <U> Observable<U> cast(Class<U> cls) {
        ObjectHelper.requireNonNull(cls, "clazz is null");
        return map(Functions.castFunction(cls));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <U> Single<U> collect(Callable<? extends U> callable, BiConsumer<? super U, ? super T> biConsumer) {
        ObjectHelper.requireNonNull(callable, "initialValueSupplier is null");
        ObjectHelper.requireNonNull(biConsumer, "collector is null");
        return RxJavaPlugins.onAssembly(new ObservableCollectSingle(this, callable, biConsumer));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <U> Single<U> collectInto(U u, BiConsumer<? super U, ? super T> biConsumer) {
        ObjectHelper.requireNonNull(u, "initialValue is null");
        return collect(Functions.justCallable(u), biConsumer);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> Observable<R> compose(ObservableTransformer<? super T, ? extends R> observableTransformer) {
        return wrap(((ObservableTransformer) ObjectHelper.requireNonNull(observableTransformer, "composer is null")).apply(this));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> Observable<R> concatMap(Function<? super T, ? extends ObservableSource<? extends R>> function) {
        return concatMap(function, 2);
    }

    /* JADX WARN: Type inference failed for: r3v0, types: [io.reactivex.functions.Function, io.reactivex.functions.Function<? super T, ? extends io.reactivex.ObservableSource<? extends R>>, java.lang.Object] */
    /* JADX WARNING: Unknown variable types count: 1 */
    @io.reactivex.annotations.CheckReturnValue
    @io.reactivex.annotations.SchedulerSupport(io.reactivex.annotations.SchedulerSupport.NONE)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <R> io.reactivex.Observable<R> concatMap(io.reactivex.functions.Function<? super T, ? extends io.reactivex.ObservableSource<? extends R>> r3, int r4) {
        /*
            r2 = this;
            java.lang.String r0 = "mapper is null"
            io.reactivex.internal.functions.ObjectHelper.requireNonNull(r3, r0)
            java.lang.String r0 = "prefetch"
            io.reactivex.internal.functions.ObjectHelper.verifyPositive(r4, r0)
            boolean r0 = r2 instanceof io.reactivex.internal.fuseable.ScalarCallable
            if (r0 == 0) goto L_0x0021
            r4 = r2
            io.reactivex.internal.fuseable.ScalarCallable r4 = (io.reactivex.internal.fuseable.ScalarCallable) r4
            java.lang.Object r4 = r4.call()
            if (r4 != 0) goto L_0x001c
            io.reactivex.Observable r3 = empty()
            return r3
        L_0x001c:
            io.reactivex.Observable r3 = io.reactivex.internal.operators.observable.ObservableScalarXMap.scalarXMap(r4, r3)
            return r3
        L_0x0021:
            io.reactivex.internal.operators.observable.ObservableConcatMap r0 = new io.reactivex.internal.operators.observable.ObservableConcatMap
            io.reactivex.internal.util.ErrorMode r1 = io.reactivex.internal.util.ErrorMode.IMMEDIATE
            r0.<init>(r2, r3, r4, r1)
            io.reactivex.Observable r3 = io.reactivex.plugins.RxJavaPlugins.onAssembly(r0)
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: io.reactivex.Observable.concatMap(io.reactivex.functions.Function, int):io.reactivex.Observable");
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> Observable<R> concatMapDelayError(Function<? super T, ? extends ObservableSource<? extends R>> function) {
        return concatMapDelayError(function, bufferSize(), true);
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [io.reactivex.functions.Function, io.reactivex.functions.Function<? super T, ? extends io.reactivex.ObservableSource<? extends R>>, java.lang.Object] */
    /* JADX WARNING: Unknown variable types count: 1 */
    @io.reactivex.annotations.CheckReturnValue
    @io.reactivex.annotations.SchedulerSupport(io.reactivex.annotations.SchedulerSupport.NONE)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <R> io.reactivex.Observable<R> concatMapDelayError(io.reactivex.functions.Function<? super T, ? extends io.reactivex.ObservableSource<? extends R>> r2, int r3, boolean r4) {
        /*
            r1 = this;
            java.lang.String r0 = "mapper is null"
            io.reactivex.internal.functions.ObjectHelper.requireNonNull(r2, r0)
            java.lang.String r0 = "prefetch"
            io.reactivex.internal.functions.ObjectHelper.verifyPositive(r3, r0)
            boolean r0 = r1 instanceof io.reactivex.internal.fuseable.ScalarCallable
            if (r0 == 0) goto L_0x0021
            r3 = r1
            io.reactivex.internal.fuseable.ScalarCallable r3 = (io.reactivex.internal.fuseable.ScalarCallable) r3
            java.lang.Object r3 = r3.call()
            if (r3 != 0) goto L_0x001c
            io.reactivex.Observable r2 = empty()
            return r2
        L_0x001c:
            io.reactivex.Observable r2 = io.reactivex.internal.operators.observable.ObservableScalarXMap.scalarXMap(r3, r2)
            return r2
        L_0x0021:
            io.reactivex.internal.operators.observable.ObservableConcatMap r0 = new io.reactivex.internal.operators.observable.ObservableConcatMap
            if (r4 == 0) goto L_0x0028
            io.reactivex.internal.util.ErrorMode r4 = io.reactivex.internal.util.ErrorMode.END
            goto L_0x002a
        L_0x0028:
            io.reactivex.internal.util.ErrorMode r4 = io.reactivex.internal.util.ErrorMode.BOUNDARY
        L_0x002a:
            r0.<init>(r1, r2, r3, r4)
            io.reactivex.Observable r2 = io.reactivex.plugins.RxJavaPlugins.onAssembly(r0)
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: io.reactivex.Observable.concatMapDelayError(io.reactivex.functions.Function, int, boolean):io.reactivex.Observable");
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> Observable<R> concatMapEager(Function<? super T, ? extends ObservableSource<? extends R>> function) {
        return concatMapEager(function, Integer.MAX_VALUE, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> Observable<R> concatMapEager(Function<? super T, ? extends ObservableSource<? extends R>> function, int i, int i2) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.verifyPositive(i, "maxConcurrency");
        ObjectHelper.verifyPositive(i2, "prefetch");
        return RxJavaPlugins.onAssembly(new ObservableConcatMapEager(this, function, ErrorMode.IMMEDIATE, i, i2));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> Observable<R> concatMapEagerDelayError(Function<? super T, ? extends ObservableSource<? extends R>> function, boolean z) {
        return concatMapEagerDelayError(function, Integer.MAX_VALUE, bufferSize(), z);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> Observable<R> concatMapEagerDelayError(Function<? super T, ? extends ObservableSource<? extends R>> function, int i, int i2, boolean z) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.verifyPositive(i, "maxConcurrency");
        ObjectHelper.verifyPositive(i2, "prefetch");
        return RxJavaPlugins.onAssembly(new ObservableConcatMapEager(this, function, z ? ErrorMode.END : ErrorMode.BOUNDARY, i, i2));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Completable concatMapCompletable(Function<? super T, ? extends CompletableSource> function) {
        return concatMapCompletable(function, 2);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Completable concatMapCompletable(Function<? super T, ? extends CompletableSource> function, int i) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.verifyPositive(i, "capacityHint");
        return RxJavaPlugins.onAssembly(new ObservableConcatMapCompletable(this, function, ErrorMode.IMMEDIATE, i));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Completable concatMapCompletableDelayError(Function<? super T, ? extends CompletableSource> function) {
        return concatMapCompletableDelayError(function, true, 2);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Completable concatMapCompletableDelayError(Function<? super T, ? extends CompletableSource> function, boolean z) {
        return concatMapCompletableDelayError(function, z, 2);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Completable concatMapCompletableDelayError(Function<? super T, ? extends CompletableSource> function, boolean z, int i) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.verifyPositive(i, "prefetch");
        return RxJavaPlugins.onAssembly(new ObservableConcatMapCompletable(this, function, z ? ErrorMode.END : ErrorMode.BOUNDARY, i));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <U> Observable<U> concatMapIterable(Function<? super T, ? extends Iterable<? extends U>> function) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        return RxJavaPlugins.onAssembly(new ObservableFlattenIterable(this, function));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <U> Observable<U> concatMapIterable(Function<? super T, ? extends Iterable<? extends U>> function, int i) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.verifyPositive(i, "prefetch");
        return concatMap(ObservableInternalHelper.flatMapIntoIterable(function), i);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> Observable<R> concatMapMaybe(Function<? super T, ? extends MaybeSource<? extends R>> function) {
        return concatMapMaybe(function, 2);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> Observable<R> concatMapMaybe(Function<? super T, ? extends MaybeSource<? extends R>> function, int i) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.verifyPositive(i, "prefetch");
        return RxJavaPlugins.onAssembly(new ObservableConcatMapMaybe(this, function, ErrorMode.IMMEDIATE, i));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> Observable<R> concatMapMaybeDelayError(Function<? super T, ? extends MaybeSource<? extends R>> function) {
        return concatMapMaybeDelayError(function, true, 2);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> Observable<R> concatMapMaybeDelayError(Function<? super T, ? extends MaybeSource<? extends R>> function, boolean z) {
        return concatMapMaybeDelayError(function, z, 2);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> Observable<R> concatMapMaybeDelayError(Function<? super T, ? extends MaybeSource<? extends R>> function, boolean z, int i) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.verifyPositive(i, "prefetch");
        return RxJavaPlugins.onAssembly(new ObservableConcatMapMaybe(this, function, z ? ErrorMode.END : ErrorMode.BOUNDARY, i));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> Observable<R> concatMapSingle(Function<? super T, ? extends SingleSource<? extends R>> function) {
        return concatMapSingle(function, 2);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> Observable<R> concatMapSingle(Function<? super T, ? extends SingleSource<? extends R>> function, int i) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.verifyPositive(i, "prefetch");
        return RxJavaPlugins.onAssembly(new ObservableConcatMapSingle(this, function, ErrorMode.IMMEDIATE, i));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> Observable<R> concatMapSingleDelayError(Function<? super T, ? extends SingleSource<? extends R>> function) {
        return concatMapSingleDelayError(function, true, 2);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> Observable<R> concatMapSingleDelayError(Function<? super T, ? extends SingleSource<? extends R>> function, boolean z) {
        return concatMapSingleDelayError(function, z, 2);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> Observable<R> concatMapSingleDelayError(Function<? super T, ? extends SingleSource<? extends R>> function, boolean z, int i) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.verifyPositive(i, "prefetch");
        return RxJavaPlugins.onAssembly(new ObservableConcatMapSingle(this, function, z ? ErrorMode.END : ErrorMode.BOUNDARY, i));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> concatWith(ObservableSource<? extends T> observableSource) {
        ObjectHelper.requireNonNull(observableSource, "other is null");
        return concat(this, observableSource);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> concatWith(SingleSource<? extends T> singleSource) {
        ObjectHelper.requireNonNull(singleSource, "other is null");
        return RxJavaPlugins.onAssembly(new ObservableConcatWithSingle(this, singleSource));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> concatWith(MaybeSource<? extends T> maybeSource) {
        ObjectHelper.requireNonNull(maybeSource, "other is null");
        return RxJavaPlugins.onAssembly(new ObservableConcatWithMaybe(this, maybeSource));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> concatWith(CompletableSource completableSource) {
        ObjectHelper.requireNonNull(completableSource, "other is null");
        return RxJavaPlugins.onAssembly(new ObservableConcatWithCompletable(this, completableSource));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Single<Boolean> contains(Object obj) {
        ObjectHelper.requireNonNull(obj, "element is null");
        return any(Functions.equalsWith(obj));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Single<Long> count() {
        return RxJavaPlugins.onAssembly(new ObservableCountSingle(this));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <U> Observable<T> debounce(Function<? super T, ? extends ObservableSource<U>> function) {
        ObjectHelper.requireNonNull(function, "debounceSelector is null");
        return RxJavaPlugins.onAssembly(new ObservableDebounce(this, function));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.COMPUTATION)
    public final Observable<T> debounce(long j, TimeUnit timeUnit) {
        return debounce(j, timeUnit, Schedulers.computation());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final Observable<T> debounce(long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new ObservableDebounceTimed(this, j, timeUnit, scheduler));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> defaultIfEmpty(T t) {
        ObjectHelper.requireNonNull(t, "defaultItem is null");
        return switchIfEmpty(just(t));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <U> Observable<T> delay(Function<? super T, ? extends ObservableSource<U>> function) {
        ObjectHelper.requireNonNull(function, "itemDelay is null");
        return flatMap(ObservableInternalHelper.itemDelay(function));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.COMPUTATION)
    public final Observable<T> delay(long j, TimeUnit timeUnit) {
        return delay(j, timeUnit, Schedulers.computation(), false);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.COMPUTATION)
    public final Observable<T> delay(long j, TimeUnit timeUnit, boolean z) {
        return delay(j, timeUnit, Schedulers.computation(), z);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final Observable<T> delay(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return delay(j, timeUnit, scheduler, false);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final Observable<T> delay(long j, TimeUnit timeUnit, Scheduler scheduler, boolean z) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new ObservableDelay(this, j, timeUnit, scheduler, z));
    }

    /* JADX WARN: Type inference failed for: r1v0, types: [io.reactivex.ObservableSource, io.reactivex.ObservableSource<U>] */
    /* JADX WARNING: Unknown variable types count: 1 */
    @io.reactivex.annotations.CheckReturnValue
    @io.reactivex.annotations.SchedulerSupport(io.reactivex.annotations.SchedulerSupport.NONE)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <U, V> io.reactivex.Observable<T> delay(io.reactivex.ObservableSource<U> r1, io.reactivex.functions.Function<? super T, ? extends io.reactivex.ObservableSource<V>> r2) {
        /*
            r0 = this;
            io.reactivex.Observable r1 = r0.delaySubscription(r1)
            io.reactivex.Observable r1 = r1.delay(r2)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: io.reactivex.Observable.delay(io.reactivex.ObservableSource, io.reactivex.functions.Function):io.reactivex.Observable");
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <U> Observable<T> delaySubscription(ObservableSource<U> observableSource) {
        ObjectHelper.requireNonNull(observableSource, "other is null");
        return RxJavaPlugins.onAssembly(new ObservableDelaySubscriptionOther(this, observableSource));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.COMPUTATION)
    public final Observable<T> delaySubscription(long j, TimeUnit timeUnit) {
        return delaySubscription(j, timeUnit, Schedulers.computation());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final Observable<T> delaySubscription(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return delaySubscription(timer(j, timeUnit, scheduler));
    }

    @CheckReturnValue
    @Deprecated
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <T2> Observable<T2> dematerialize() {
        return RxJavaPlugins.onAssembly(new ObservableDematerialize(this, Functions.identity()));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> Observable<R> dematerialize(Function<? super T, Notification<R>> function) {
        ObjectHelper.requireNonNull(function, "selector is null");
        return RxJavaPlugins.onAssembly(new ObservableDematerialize(this, function));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> distinct() {
        return distinct(Functions.identity(), Functions.createHashSet());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <K> Observable<T> distinct(Function<? super T, K> function) {
        return distinct(function, Functions.createHashSet());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <K> Observable<T> distinct(Function<? super T, K> function, Callable<? extends Collection<? super K>> callable) {
        ObjectHelper.requireNonNull(function, "keySelector is null");
        ObjectHelper.requireNonNull(callable, "collectionSupplier is null");
        return RxJavaPlugins.onAssembly(new ObservableDistinct(this, function, callable));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> distinctUntilChanged() {
        return distinctUntilChanged(Functions.identity());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <K> Observable<T> distinctUntilChanged(Function<? super T, K> function) {
        ObjectHelper.requireNonNull(function, "keySelector is null");
        return RxJavaPlugins.onAssembly(new ObservableDistinctUntilChanged(this, function, ObjectHelper.equalsPredicate()));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> distinctUntilChanged(BiPredicate<? super T, ? super T> biPredicate) {
        ObjectHelper.requireNonNull(biPredicate, "comparer is null");
        return RxJavaPlugins.onAssembly(new ObservableDistinctUntilChanged(this, Functions.identity(), biPredicate));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> doAfterNext(Consumer<? super T> consumer) {
        ObjectHelper.requireNonNull(consumer, "onAfterNext is null");
        return RxJavaPlugins.onAssembly(new ObservableDoAfterNext(this, consumer));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> doAfterTerminate(Action action) {
        ObjectHelper.requireNonNull(action, "onFinally is null");
        return doOnEach(Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.EMPTY_ACTION, action);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> doFinally(Action action) {
        ObjectHelper.requireNonNull(action, "onFinally is null");
        return RxJavaPlugins.onAssembly(new ObservableDoFinally(this, action));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> doOnDispose(Action action) {
        return doOnLifecycle(Functions.emptyConsumer(), action);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> doOnComplete(Action action) {
        return doOnEach(Functions.emptyConsumer(), Functions.emptyConsumer(), action, Functions.EMPTY_ACTION);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    private Observable<T> doOnEach(Consumer<? super T> consumer, Consumer<? super Throwable> consumer2, Action action, Action action2) {
        ObjectHelper.requireNonNull(consumer, "onNext is null");
        ObjectHelper.requireNonNull(consumer2, "onError is null");
        ObjectHelper.requireNonNull(action, "onComplete is null");
        ObjectHelper.requireNonNull(action2, "onAfterTerminate is null");
        return RxJavaPlugins.onAssembly(new ObservableDoOnEach(this, consumer, consumer2, action, action2));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> doOnEach(Consumer<? super Notification<T>> consumer) {
        ObjectHelper.requireNonNull(consumer, "onNotification is null");
        return doOnEach(Functions.notificationOnNext(consumer), Functions.notificationOnError(consumer), Functions.notificationOnComplete(consumer), Functions.EMPTY_ACTION);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> doOnEach(Observer<? super T> observer) {
        ObjectHelper.requireNonNull(observer, "observer is null");
        return doOnEach(ObservableInternalHelper.observerOnNext(observer), ObservableInternalHelper.observerOnError(observer), ObservableInternalHelper.observerOnComplete(observer), Functions.EMPTY_ACTION);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> doOnError(Consumer<? super Throwable> consumer) {
        return doOnEach(Functions.emptyConsumer(), consumer, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> doOnLifecycle(Consumer<? super Disposable> consumer, Action action) {
        ObjectHelper.requireNonNull(consumer, "onSubscribe is null");
        ObjectHelper.requireNonNull(action, "onDispose is null");
        return RxJavaPlugins.onAssembly(new ObservableDoOnLifecycle(this, consumer, action));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> doOnNext(Consumer<? super T> consumer) {
        return doOnEach(consumer, Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> doOnSubscribe(Consumer<? super Disposable> consumer) {
        return doOnLifecycle(consumer, Functions.EMPTY_ACTION);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> doOnTerminate(Action action) {
        ObjectHelper.requireNonNull(action, "onTerminate is null");
        return doOnEach(Functions.emptyConsumer(), Functions.actionConsumer(action), action, Functions.EMPTY_ACTION);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Maybe<T> elementAt(long j) {
        if (j >= 0) {
            return RxJavaPlugins.onAssembly(new ObservableElementAtMaybe(this, j));
        }
        throw new IndexOutOfBoundsException("index >= 0 required but it was " + j);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Single<T> elementAt(long j, T t) {
        if (j >= 0) {
            ObjectHelper.requireNonNull(t, "defaultItem is null");
            return RxJavaPlugins.onAssembly(new ObservableElementAtSingle(this, j, t));
        }
        throw new IndexOutOfBoundsException("index >= 0 required but it was " + j);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Single<T> elementAtOrError(long j) {
        if (j >= 0) {
            return RxJavaPlugins.onAssembly(new ObservableElementAtSingle(this, j, null));
        }
        throw new IndexOutOfBoundsException("index >= 0 required but it was " + j);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> filter(Predicate<? super T> predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly(new ObservableFilter(this, predicate));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Maybe<T> firstElement() {
        return elementAt(0);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Single<T> first(T t) {
        return elementAt(0, t);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Single<T> firstOrError() {
        return elementAtOrError(0);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: io.reactivex.Observable.flatMap(io.reactivex.functions.Function, boolean):io.reactivex.Observable<R>
     arg types: [io.reactivex.functions.Function<? super T, ? extends io.reactivex.ObservableSource<? extends R>>, int]
     candidates:
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, int):io.reactivex.Observable<R>
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, io.reactivex.functions.BiFunction):io.reactivex.Observable<R>
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, boolean):io.reactivex.Observable<R> */
    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> Observable<R> flatMap(Function<? super T, ? extends ObservableSource<? extends R>> function) {
        return flatMap((Function) function, false);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> Observable<R> flatMap(Function<? super T, ? extends ObservableSource<? extends R>> function, boolean z) {
        return flatMap(function, z, Integer.MAX_VALUE);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> Observable<R> flatMap(Function<? super T, ? extends ObservableSource<? extends R>> function, boolean z, int i) {
        return flatMap(function, z, i, bufferSize());
    }

    /* JADX WARN: Type inference failed for: r8v0, types: [io.reactivex.functions.Function, io.reactivex.functions.Function<? super T, ? extends io.reactivex.ObservableSource<? extends R>>, java.lang.Object] */
    /* JADX WARNING: Unknown variable types count: 1 */
    @io.reactivex.annotations.CheckReturnValue
    @io.reactivex.annotations.SchedulerSupport(io.reactivex.annotations.SchedulerSupport.NONE)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <R> io.reactivex.Observable<R> flatMap(io.reactivex.functions.Function<? super T, ? extends io.reactivex.ObservableSource<? extends R>> r8, boolean r9, int r10, int r11) {
        /*
            r7 = this;
            java.lang.String r0 = "mapper is null"
            io.reactivex.internal.functions.ObjectHelper.requireNonNull(r8, r0)
            java.lang.String r0 = "maxConcurrency"
            io.reactivex.internal.functions.ObjectHelper.verifyPositive(r10, r0)
            java.lang.String r0 = "bufferSize"
            io.reactivex.internal.functions.ObjectHelper.verifyPositive(r11, r0)
            boolean r0 = r7 instanceof io.reactivex.internal.fuseable.ScalarCallable
            if (r0 == 0) goto L_0x0026
            r9 = r7
            io.reactivex.internal.fuseable.ScalarCallable r9 = (io.reactivex.internal.fuseable.ScalarCallable) r9
            java.lang.Object r9 = r9.call()
            if (r9 != 0) goto L_0x0021
            io.reactivex.Observable r8 = empty()
            return r8
        L_0x0021:
            io.reactivex.Observable r8 = io.reactivex.internal.operators.observable.ObservableScalarXMap.scalarXMap(r9, r8)
            return r8
        L_0x0026:
            io.reactivex.internal.operators.observable.ObservableFlatMap r6 = new io.reactivex.internal.operators.observable.ObservableFlatMap
            r0 = r6
            r1 = r7
            r2 = r8
            r3 = r9
            r4 = r10
            r5 = r11
            r0.<init>(r1, r2, r3, r4, r5)
            io.reactivex.Observable r8 = io.reactivex.plugins.RxJavaPlugins.onAssembly(r6)
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: io.reactivex.Observable.flatMap(io.reactivex.functions.Function, boolean, int, int):io.reactivex.Observable");
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> Observable<R> flatMap(Function<? super T, ? extends ObservableSource<? extends R>> function, Function<? super Throwable, ? extends ObservableSource<? extends R>> function2, Callable<? extends ObservableSource<? extends R>> callable) {
        ObjectHelper.requireNonNull(function, "onNextMapper is null");
        ObjectHelper.requireNonNull(function2, "onErrorMapper is null");
        ObjectHelper.requireNonNull(callable, "onCompleteSupplier is null");
        return merge(new ObservableMapNotification(this, function, function2, callable));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> Observable<R> flatMap(Function<? super T, ? extends ObservableSource<? extends R>> function, Function<Throwable, ? extends ObservableSource<? extends R>> function2, Callable<? extends ObservableSource<? extends R>> callable, int i) {
        ObjectHelper.requireNonNull(function, "onNextMapper is null");
        ObjectHelper.requireNonNull(function2, "onErrorMapper is null");
        ObjectHelper.requireNonNull(callable, "onCompleteSupplier is null");
        return merge(new ObservableMapNotification(this, function, function2, callable), i);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: io.reactivex.Observable.flatMap(io.reactivex.functions.Function, boolean, int, int):io.reactivex.Observable<R>
     arg types: [io.reactivex.functions.Function<? super T, ? extends io.reactivex.ObservableSource<? extends R>>, int, int, int]
     candidates:
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, io.reactivex.functions.BiFunction, boolean, int):io.reactivex.Observable<R>
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, io.reactivex.functions.Function, java.util.concurrent.Callable, int):io.reactivex.Observable<R>
      io.reactivex.Observable.flatMap(io.reactivex.functions.Function, boolean, int, int):io.reactivex.Observable<R> */
    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> Observable<R> flatMap(Function<? super T, ? extends ObservableSource<? extends R>> function, int i) {
        return flatMap((Function) function, false, i, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <U, R> Observable<R> flatMap(Function<? super T, ? extends ObservableSource<? extends U>> function, BiFunction<? super T, ? super U, ? extends R> biFunction) {
        return flatMap(function, biFunction, false, bufferSize(), bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <U, R> Observable<R> flatMap(Function<? super T, ? extends ObservableSource<? extends U>> function, BiFunction<? super T, ? super U, ? extends R> biFunction, boolean z) {
        return flatMap(function, biFunction, z, bufferSize(), bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <U, R> Observable<R> flatMap(Function<? super T, ? extends ObservableSource<? extends U>> function, BiFunction<? super T, ? super U, ? extends R> biFunction, boolean z, int i) {
        return flatMap(function, biFunction, z, i, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <U, R> Observable<R> flatMap(Function<? super T, ? extends ObservableSource<? extends U>> function, BiFunction<? super T, ? super U, ? extends R> biFunction, boolean z, int i, int i2) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.requireNonNull(biFunction, "combiner is null");
        return flatMap(ObservableInternalHelper.flatMapWithCombiner(function, biFunction), z, i, i2);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <U, R> Observable<R> flatMap(Function<? super T, ? extends ObservableSource<? extends U>> function, BiFunction<? super T, ? super U, ? extends R> biFunction, int i) {
        return flatMap(function, biFunction, false, i, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Completable flatMapCompletable(Function<? super T, ? extends CompletableSource> function) {
        return flatMapCompletable(function, false);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Completable flatMapCompletable(Function<? super T, ? extends CompletableSource> function, boolean z) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        return RxJavaPlugins.onAssembly(new ObservableFlatMapCompletableCompletable(this, function, z));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <U> Observable<U> flatMapIterable(Function<? super T, ? extends Iterable<? extends U>> function) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        return RxJavaPlugins.onAssembly(new ObservableFlattenIterable(this, function));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <U, V> Observable<V> flatMapIterable(Function<? super T, ? extends Iterable<? extends U>> function, BiFunction<? super T, ? super U, ? extends V> biFunction) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.requireNonNull(biFunction, "resultSelector is null");
        return flatMap(ObservableInternalHelper.flatMapIntoIterable(function), biFunction, false, bufferSize(), bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> Observable<R> flatMapMaybe(Function<? super T, ? extends MaybeSource<? extends R>> function) {
        return flatMapMaybe(function, false);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> Observable<R> flatMapMaybe(Function<? super T, ? extends MaybeSource<? extends R>> function, boolean z) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        return RxJavaPlugins.onAssembly(new ObservableFlatMapMaybe(this, function, z));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> Observable<R> flatMapSingle(Function<? super T, ? extends SingleSource<? extends R>> function) {
        return flatMapSingle(function, false);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> Observable<R> flatMapSingle(Function<? super T, ? extends SingleSource<? extends R>> function, boolean z) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        return RxJavaPlugins.onAssembly(new ObservableFlatMapSingle(this, function, z));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Disposable forEach(Consumer<? super T> consumer) {
        return subscribe(consumer);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Disposable forEachWhile(Predicate<? super T> predicate) {
        return forEachWhile(predicate, Functions.ON_ERROR_MISSING, Functions.EMPTY_ACTION);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Disposable forEachWhile(Predicate<? super T> predicate, Consumer<? super Throwable> consumer) {
        return forEachWhile(predicate, consumer, Functions.EMPTY_ACTION);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Disposable forEachWhile(Predicate<? super T> predicate, Consumer<? super Throwable> consumer, Action action) {
        ObjectHelper.requireNonNull(predicate, "onNext is null");
        ObjectHelper.requireNonNull(consumer, "onError is null");
        ObjectHelper.requireNonNull(action, "onComplete is null");
        ForEachWhileObserver forEachWhileObserver = new ForEachWhileObserver(predicate, consumer, action);
        subscribe(forEachWhileObserver);
        return forEachWhileObserver;
    }

    /* JADX WARN: Type inference failed for: r4v0, types: [io.reactivex.functions.Function, io.reactivex.functions.Function<? super T, ? extends K>] */
    /* JADX WARNING: Unknown variable types count: 1 */
    @io.reactivex.annotations.CheckReturnValue
    @io.reactivex.annotations.SchedulerSupport(io.reactivex.annotations.SchedulerSupport.NONE)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <K> io.reactivex.Observable<io.reactivex.observables.GroupedObservable<K, T>> groupBy(io.reactivex.functions.Function<? super T, ? extends K> r4) {
        /*
            r3 = this;
            io.reactivex.functions.Function r0 = io.reactivex.internal.functions.Functions.identity()
            int r1 = bufferSize()
            r2 = 0
            io.reactivex.Observable r4 = r3.groupBy(r4, r0, r2, r1)
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: io.reactivex.Observable.groupBy(io.reactivex.functions.Function):io.reactivex.Observable");
    }

    /* JADX WARN: Type inference failed for: r3v0, types: [io.reactivex.functions.Function, io.reactivex.functions.Function<? super T, ? extends K>] */
    /* JADX WARNING: Unknown variable types count: 1 */
    @io.reactivex.annotations.CheckReturnValue
    @io.reactivex.annotations.SchedulerSupport(io.reactivex.annotations.SchedulerSupport.NONE)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <K> io.reactivex.Observable<io.reactivex.observables.GroupedObservable<K, T>> groupBy(io.reactivex.functions.Function<? super T, ? extends K> r3, boolean r4) {
        /*
            r2 = this;
            io.reactivex.functions.Function r0 = io.reactivex.internal.functions.Functions.identity()
            int r1 = bufferSize()
            io.reactivex.Observable r3 = r2.groupBy(r3, r0, r4, r1)
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: io.reactivex.Observable.groupBy(io.reactivex.functions.Function, boolean):io.reactivex.Observable");
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <K, V> Observable<GroupedObservable<K, V>> groupBy(Function<? super T, ? extends K> function, Function<? super T, ? extends V> function2) {
        return groupBy(function, function2, false, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <K, V> Observable<GroupedObservable<K, V>> groupBy(Function<? super T, ? extends K> function, Function<? super T, ? extends V> function2, boolean z) {
        return groupBy(function, function2, z, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <K, V> Observable<GroupedObservable<K, V>> groupBy(Function<? super T, ? extends K> function, Function<? super T, ? extends V> function2, boolean z, int i) {
        ObjectHelper.requireNonNull(function, "keySelector is null");
        ObjectHelper.requireNonNull(function2, "valueSelector is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        return RxJavaPlugins.onAssembly(new ObservableGroupBy(this, function, function2, i, z));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <TRight, TLeftEnd, TRightEnd, R> Observable<R> groupJoin(ObservableSource<? extends TRight> observableSource, Function<? super T, ? extends ObservableSource<TLeftEnd>> function, Function<? super TRight, ? extends ObservableSource<TRightEnd>> function2, BiFunction<? super T, ? super Observable<TRight>, ? extends R> biFunction) {
        ObjectHelper.requireNonNull(observableSource, "other is null");
        ObjectHelper.requireNonNull(function, "leftEnd is null");
        ObjectHelper.requireNonNull(function2, "rightEnd is null");
        ObjectHelper.requireNonNull(biFunction, "resultSelector is null");
        return RxJavaPlugins.onAssembly(new ObservableGroupJoin(this, observableSource, function, function2, biFunction));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> hide() {
        return RxJavaPlugins.onAssembly(new ObservableHide(this));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Completable ignoreElements() {
        return RxJavaPlugins.onAssembly(new ObservableIgnoreElementsCompletable(this));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Single<Boolean> isEmpty() {
        return all(Functions.alwaysFalse());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <TRight, TLeftEnd, TRightEnd, R> Observable<R> join(ObservableSource<? extends TRight> observableSource, Function<? super T, ? extends ObservableSource<TLeftEnd>> function, Function<? super TRight, ? extends ObservableSource<TRightEnd>> function2, BiFunction<? super T, ? super TRight, ? extends R> biFunction) {
        ObjectHelper.requireNonNull(observableSource, "other is null");
        ObjectHelper.requireNonNull(function, "leftEnd is null");
        ObjectHelper.requireNonNull(function2, "rightEnd is null");
        ObjectHelper.requireNonNull(biFunction, "resultSelector is null");
        return RxJavaPlugins.onAssembly(new ObservableJoin(this, observableSource, function, function2, biFunction));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Maybe<T> lastElement() {
        return RxJavaPlugins.onAssembly(new ObservableLastMaybe(this));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Single<T> last(T t) {
        ObjectHelper.requireNonNull(t, "defaultItem is null");
        return RxJavaPlugins.onAssembly(new ObservableLastSingle(this, t));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Single<T> lastOrError() {
        return RxJavaPlugins.onAssembly(new ObservableLastSingle(this, null));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> Observable<R> lift(ObservableOperator<? extends R, ? super T> observableOperator) {
        ObjectHelper.requireNonNull(observableOperator, "lifter is null");
        return RxJavaPlugins.onAssembly(new ObservableLift(this, observableOperator));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> Observable<R> map(Function<? super T, ? extends R> function) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        return RxJavaPlugins.onAssembly(new ObservableMap(this, function));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<Notification<T>> materialize() {
        return RxJavaPlugins.onAssembly(new ObservableMaterialize(this));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> mergeWith(ObservableSource<? extends T> observableSource) {
        ObjectHelper.requireNonNull(observableSource, "other is null");
        return merge(this, observableSource);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> mergeWith(SingleSource<? extends T> singleSource) {
        ObjectHelper.requireNonNull(singleSource, "other is null");
        return RxJavaPlugins.onAssembly(new ObservableMergeWithSingle(this, singleSource));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> mergeWith(MaybeSource<? extends T> maybeSource) {
        ObjectHelper.requireNonNull(maybeSource, "other is null");
        return RxJavaPlugins.onAssembly(new ObservableMergeWithMaybe(this, maybeSource));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> mergeWith(CompletableSource completableSource) {
        ObjectHelper.requireNonNull(completableSource, "other is null");
        return RxJavaPlugins.onAssembly(new ObservableMergeWithCompletable(this, completableSource));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final Observable<T> observeOn(Scheduler scheduler) {
        return observeOn(scheduler, false, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final Observable<T> observeOn(Scheduler scheduler, boolean z) {
        return observeOn(scheduler, z, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final Observable<T> observeOn(Scheduler scheduler, boolean z, int i) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        return RxJavaPlugins.onAssembly(new ObservableObserveOn(this, scheduler, z, i));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <U> Observable<U> ofType(Class<U> cls) {
        ObjectHelper.requireNonNull(cls, "clazz is null");
        return filter(Functions.isInstanceOf(cls)).cast(cls);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> onErrorResumeNext(Function<? super Throwable, ? extends ObservableSource<? extends T>> function) {
        ObjectHelper.requireNonNull(function, "resumeFunction is null");
        return RxJavaPlugins.onAssembly(new ObservableOnErrorNext(this, function, false));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> onErrorResumeNext(ObservableSource<? extends T> observableSource) {
        ObjectHelper.requireNonNull(observableSource, "next is null");
        return onErrorResumeNext(Functions.justFunction(observableSource));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> onErrorReturn(Function<? super Throwable, ? extends T> function) {
        ObjectHelper.requireNonNull(function, "valueSupplier is null");
        return RxJavaPlugins.onAssembly(new ObservableOnErrorReturn(this, function));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> onErrorReturnItem(T t) {
        ObjectHelper.requireNonNull(t, "item is null");
        return onErrorReturn(Functions.justFunction(t));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> onExceptionResumeNext(ObservableSource<? extends T> observableSource) {
        ObjectHelper.requireNonNull(observableSource, "next is null");
        return RxJavaPlugins.onAssembly(new ObservableOnErrorNext(this, Functions.justFunction(observableSource), true));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> onTerminateDetach() {
        return RxJavaPlugins.onAssembly(new ObservableDetach(this));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final ConnectableObservable<T> publish() {
        return ObservablePublish.create(this);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> Observable<R> publish(Function<? super Observable<T>, ? extends ObservableSource<R>> function) {
        ObjectHelper.requireNonNull(function, "selector is null");
        return RxJavaPlugins.onAssembly(new ObservablePublishSelector(this, function));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Maybe<T> reduce(BiFunction<T, T, T> biFunction) {
        ObjectHelper.requireNonNull(biFunction, "reducer is null");
        return RxJavaPlugins.onAssembly(new ObservableReduceMaybe(this, biFunction));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> Single<R> reduce(R r, BiFunction<R, ? super T, R> biFunction) {
        ObjectHelper.requireNonNull(r, "seed is null");
        ObjectHelper.requireNonNull(biFunction, "reducer is null");
        return RxJavaPlugins.onAssembly(new ObservableReduceSeedSingle(this, r, biFunction));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> Single<R> reduceWith(Callable<R> callable, BiFunction<R, ? super T, R> biFunction) {
        ObjectHelper.requireNonNull(callable, "seedSupplier is null");
        ObjectHelper.requireNonNull(biFunction, "reducer is null");
        return RxJavaPlugins.onAssembly(new ObservableReduceWithSingle(this, callable, biFunction));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> repeat() {
        return repeat(LongCompanionObject.MAX_VALUE);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> repeat(long j) {
        int i = (j > 0 ? 1 : (j == 0 ? 0 : -1));
        if (i < 0) {
            throw new IllegalArgumentException("times >= 0 required but it was " + j);
        } else if (i == 0) {
            return empty();
        } else {
            return RxJavaPlugins.onAssembly(new ObservableRepeat(this, j));
        }
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> repeatUntil(BooleanSupplier booleanSupplier) {
        ObjectHelper.requireNonNull(booleanSupplier, "stop is null");
        return RxJavaPlugins.onAssembly(new ObservableRepeatUntil(this, booleanSupplier));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> repeatWhen(Function<? super Observable<Object>, ? extends ObservableSource<?>> function) {
        ObjectHelper.requireNonNull(function, "handler is null");
        return RxJavaPlugins.onAssembly(new ObservableRepeatWhen(this, function));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final ConnectableObservable<T> replay() {
        return ObservableReplay.createFrom(this);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> Observable<R> replay(Function<? super Observable<T>, ? extends ObservableSource<R>> function) {
        ObjectHelper.requireNonNull(function, "selector is null");
        return ObservableReplay.multicastSelector(ObservableInternalHelper.replayCallable(this), function);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> Observable<R> replay(Function<? super Observable<T>, ? extends ObservableSource<R>> function, int i) {
        ObjectHelper.requireNonNull(function, "selector is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        return ObservableReplay.multicastSelector(ObservableInternalHelper.replayCallable(this, i), function);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.COMPUTATION)
    public final <R> Observable<R> replay(Function<? super Observable<T>, ? extends ObservableSource<R>> function, int i, long j, TimeUnit timeUnit) {
        return replay(function, i, j, timeUnit, Schedulers.computation());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final <R> Observable<R> replay(Function<? super Observable<T>, ? extends ObservableSource<R>> function, int i, long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(function, "selector is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return ObservableReplay.multicastSelector(ObservableInternalHelper.replayCallable(this, i, j, timeUnit, scheduler), function);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final <R> Observable<R> replay(Function<? super Observable<T>, ? extends ObservableSource<R>> function, int i, Scheduler scheduler) {
        ObjectHelper.requireNonNull(function, "selector is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        return ObservableReplay.multicastSelector(ObservableInternalHelper.replayCallable(this, i), ObservableInternalHelper.replayFunction(function, scheduler));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.COMPUTATION)
    public final <R> Observable<R> replay(Function<? super Observable<T>, ? extends ObservableSource<R>> function, long j, TimeUnit timeUnit) {
        return replay(function, j, timeUnit, Schedulers.computation());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final <R> Observable<R> replay(Function<? super Observable<T>, ? extends ObservableSource<R>> function, long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(function, "selector is null");
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return ObservableReplay.multicastSelector(ObservableInternalHelper.replayCallable(this, j, timeUnit, scheduler), function);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final <R> Observable<R> replay(Function<? super Observable<T>, ? extends ObservableSource<R>> function, Scheduler scheduler) {
        ObjectHelper.requireNonNull(function, "selector is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return ObservableReplay.multicastSelector(ObservableInternalHelper.replayCallable(this), ObservableInternalHelper.replayFunction(function, scheduler));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final ConnectableObservable<T> replay(int i) {
        ObjectHelper.verifyPositive(i, "bufferSize");
        return ObservableReplay.create(this, i);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.COMPUTATION)
    public final ConnectableObservable<T> replay(int i, long j, TimeUnit timeUnit) {
        return replay(i, j, timeUnit, Schedulers.computation());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final ConnectableObservable<T> replay(int i, long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.verifyPositive(i, "bufferSize");
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return ObservableReplay.create(this, j, timeUnit, scheduler, i);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final ConnectableObservable<T> replay(int i, Scheduler scheduler) {
        ObjectHelper.verifyPositive(i, "bufferSize");
        return ObservableReplay.observeOn(replay(i), scheduler);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.COMPUTATION)
    public final ConnectableObservable<T> replay(long j, TimeUnit timeUnit) {
        return replay(j, timeUnit, Schedulers.computation());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final ConnectableObservable<T> replay(long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return ObservableReplay.create(this, j, timeUnit, scheduler);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final ConnectableObservable<T> replay(Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return ObservableReplay.observeOn(replay(), scheduler);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> retry() {
        return retry(LongCompanionObject.MAX_VALUE, Functions.alwaysTrue());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> retry(BiPredicate<? super Integer, ? super Throwable> biPredicate) {
        ObjectHelper.requireNonNull(biPredicate, "predicate is null");
        return RxJavaPlugins.onAssembly(new ObservableRetryBiPredicate(this, biPredicate));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> retry(long j) {
        return retry(j, Functions.alwaysTrue());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> retry(long j, Predicate<? super Throwable> predicate) {
        if (j >= 0) {
            ObjectHelper.requireNonNull(predicate, "predicate is null");
            return RxJavaPlugins.onAssembly(new ObservableRetryPredicate(this, j, predicate));
        }
        throw new IllegalArgumentException("times >= 0 required but it was " + j);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> retry(Predicate<? super Throwable> predicate) {
        return retry(LongCompanionObject.MAX_VALUE, predicate);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> retryUntil(BooleanSupplier booleanSupplier) {
        ObjectHelper.requireNonNull(booleanSupplier, "stop is null");
        return retry(LongCompanionObject.MAX_VALUE, Functions.predicateReverseFor(booleanSupplier));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> retryWhen(Function<? super Observable<Throwable>, ? extends ObservableSource<?>> function) {
        ObjectHelper.requireNonNull(function, "handler is null");
        return RxJavaPlugins.onAssembly(new ObservableRetryWhen(this, function));
    }

    @SchedulerSupport(SchedulerSupport.NONE)
    public final void safeSubscribe(Observer<? super T> observer) {
        ObjectHelper.requireNonNull(observer, "observer is null");
        if (observer instanceof SafeObserver) {
            subscribe(observer);
        } else {
            subscribe(new SafeObserver(observer));
        }
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.COMPUTATION)
    public final Observable<T> sample(long j, TimeUnit timeUnit) {
        return sample(j, timeUnit, Schedulers.computation());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.COMPUTATION)
    public final Observable<T> sample(long j, TimeUnit timeUnit, boolean z) {
        return sample(j, timeUnit, Schedulers.computation(), z);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final Observable<T> sample(long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new ObservableSampleTimed(this, j, timeUnit, scheduler, false));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final Observable<T> sample(long j, TimeUnit timeUnit, Scheduler scheduler, boolean z) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new ObservableSampleTimed(this, j, timeUnit, scheduler, z));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <U> Observable<T> sample(ObservableSource<U> observableSource) {
        ObjectHelper.requireNonNull(observableSource, "sampler is null");
        return RxJavaPlugins.onAssembly(new ObservableSampleWithObservable(this, observableSource, false));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <U> Observable<T> sample(ObservableSource<U> observableSource, boolean z) {
        ObjectHelper.requireNonNull(observableSource, "sampler is null");
        return RxJavaPlugins.onAssembly(new ObservableSampleWithObservable(this, observableSource, z));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> scan(BiFunction<T, T, T> biFunction) {
        ObjectHelper.requireNonNull(biFunction, "accumulator is null");
        return RxJavaPlugins.onAssembly(new ObservableScan(this, biFunction));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> Observable<R> scan(R r, BiFunction<R, ? super T, R> biFunction) {
        ObjectHelper.requireNonNull(r, "initialValue is null");
        return scanWith(Functions.justCallable(r), biFunction);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> Observable<R> scanWith(Callable<R> callable, BiFunction<R, ? super T, R> biFunction) {
        ObjectHelper.requireNonNull(callable, "seedSupplier is null");
        ObjectHelper.requireNonNull(biFunction, "accumulator is null");
        return RxJavaPlugins.onAssembly(new ObservableScanSeed(this, callable, biFunction));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> serialize() {
        return RxJavaPlugins.onAssembly(new ObservableSerialized(this));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> share() {
        return publish().refCount();
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Maybe<T> singleElement() {
        return RxJavaPlugins.onAssembly(new ObservableSingleMaybe(this));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Single<T> single(T t) {
        ObjectHelper.requireNonNull(t, "defaultItem is null");
        return RxJavaPlugins.onAssembly(new ObservableSingleSingle(this, t));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Single<T> singleOrError() {
        return RxJavaPlugins.onAssembly(new ObservableSingleSingle(this, null));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> skip(long j) {
        if (j <= 0) {
            return RxJavaPlugins.onAssembly(this);
        }
        return RxJavaPlugins.onAssembly(new ObservableSkip(this, j));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.COMPUTATION)
    public final Observable<T> skip(long j, TimeUnit timeUnit) {
        return skipUntil(timer(j, timeUnit));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final Observable<T> skip(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return skipUntil(timer(j, timeUnit, scheduler));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> skipLast(int i) {
        if (i < 0) {
            throw new IndexOutOfBoundsException("count >= 0 required but it was " + i);
        } else if (i == 0) {
            return RxJavaPlugins.onAssembly(this);
        } else {
            return RxJavaPlugins.onAssembly(new ObservableSkipLast(this, i));
        }
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.TRAMPOLINE)
    public final Observable<T> skipLast(long j, TimeUnit timeUnit) {
        return skipLast(j, timeUnit, Schedulers.trampoline(), false, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.TRAMPOLINE)
    public final Observable<T> skipLast(long j, TimeUnit timeUnit, boolean z) {
        return skipLast(j, timeUnit, Schedulers.trampoline(), z, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final Observable<T> skipLast(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return skipLast(j, timeUnit, scheduler, false, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final Observable<T> skipLast(long j, TimeUnit timeUnit, Scheduler scheduler, boolean z) {
        return skipLast(j, timeUnit, scheduler, z, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final Observable<T> skipLast(long j, TimeUnit timeUnit, Scheduler scheduler, boolean z, int i) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        return RxJavaPlugins.onAssembly(new ObservableSkipLastTimed(this, j, timeUnit, scheduler, i << 1, z));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <U> Observable<T> skipUntil(ObservableSource<U> observableSource) {
        ObjectHelper.requireNonNull(observableSource, "other is null");
        return RxJavaPlugins.onAssembly(new ObservableSkipUntil(this, observableSource));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> skipWhile(Predicate<? super T> predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly(new ObservableSkipWhile(this, predicate));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> sorted() {
        return toList().toObservable().map(Functions.listSorter(Functions.naturalComparator())).flatMapIterable(Functions.identity());
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [java.util.Comparator<? super T>, java.lang.Object, java.util.Comparator] */
    /* JADX WARNING: Unknown variable types count: 1 */
    @io.reactivex.annotations.CheckReturnValue
    @io.reactivex.annotations.SchedulerSupport(io.reactivex.annotations.SchedulerSupport.NONE)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final io.reactivex.Observable<T> sorted(java.util.Comparator<? super T> r2) {
        /*
            r1 = this;
            java.lang.String r0 = "sortFunction is null"
            io.reactivex.internal.functions.ObjectHelper.requireNonNull(r2, r0)
            io.reactivex.Single r0 = r1.toList()
            io.reactivex.Observable r0 = r0.toObservable()
            io.reactivex.functions.Function r2 = io.reactivex.internal.functions.Functions.listSorter(r2)
            io.reactivex.Observable r2 = r0.map(r2)
            io.reactivex.functions.Function r0 = io.reactivex.internal.functions.Functions.identity()
            io.reactivex.Observable r2 = r2.flatMapIterable(r0)
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: io.reactivex.Observable.sorted(java.util.Comparator):io.reactivex.Observable");
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> startWith(Iterable<? extends T> iterable) {
        return concatArray(fromIterable(iterable), this);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> startWith(ObservableSource<? extends T> observableSource) {
        ObjectHelper.requireNonNull(observableSource, "other is null");
        return concatArray(observableSource, this);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> startWith(T t) {
        ObjectHelper.requireNonNull(t, "item is null");
        return concatArray(just(t), this);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> startWithArray(T... tArr) {
        Observable fromArray = fromArray(tArr);
        if (fromArray == empty()) {
            return RxJavaPlugins.onAssembly(this);
        }
        return concatArray(fromArray, this);
    }

    @SchedulerSupport(SchedulerSupport.NONE)
    public final Disposable subscribe() {
        return subscribe(Functions.emptyConsumer(), Functions.ON_ERROR_MISSING, Functions.EMPTY_ACTION, Functions.emptyConsumer());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Disposable subscribe(Consumer<? super T> consumer) {
        return subscribe(consumer, Functions.ON_ERROR_MISSING, Functions.EMPTY_ACTION, Functions.emptyConsumer());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Disposable subscribe(Consumer<? super T> consumer, Consumer<? super Throwable> consumer2) {
        return subscribe(consumer, consumer2, Functions.EMPTY_ACTION, Functions.emptyConsumer());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Disposable subscribe(Consumer<? super T> consumer, Consumer<? super Throwable> consumer2, Action action) {
        return subscribe(consumer, consumer2, action, Functions.emptyConsumer());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Disposable subscribe(Consumer<? super T> consumer, Consumer<? super Throwable> consumer2, Action action, Consumer<? super Disposable> consumer3) {
        ObjectHelper.requireNonNull(consumer, "onNext is null");
        ObjectHelper.requireNonNull(consumer2, "onError is null");
        ObjectHelper.requireNonNull(action, "onComplete is null");
        ObjectHelper.requireNonNull(consumer3, "onSubscribe is null");
        LambdaObserver lambdaObserver = new LambdaObserver(consumer, consumer2, action, consumer3);
        subscribe(lambdaObserver);
        return lambdaObserver;
    }

    @SchedulerSupport(SchedulerSupport.NONE)
    public final void subscribe(Observer<? super T> observer) {
        ObjectHelper.requireNonNull(observer, "observer is null");
        try {
            Observer<? super Object> onSubscribe = RxJavaPlugins.onSubscribe(this, observer);
            ObjectHelper.requireNonNull(onSubscribe, "The RxJavaPlugins.onSubscribe hook returned a null Observer. Please change the handler provided to RxJavaPlugins.setOnObservableSubscribe for invalid null returns. Further reading: https://github.com/ReactiveX/RxJava/wiki/Plugins");
            subscribeActual(onSubscribe);
        } catch (NullPointerException e) {
            throw e;
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            RxJavaPlugins.onError(th);
            NullPointerException nullPointerException = new NullPointerException("Actually not, but can't throw other exceptions due to RS");
            nullPointerException.initCause(th);
            throw nullPointerException;
        }
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <E extends Observer<? super T>> E subscribeWith(E e) {
        subscribe((Observer) e);
        return e;
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final Observable<T> subscribeOn(Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new ObservableSubscribeOn(this, scheduler));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> switchIfEmpty(ObservableSource<? extends T> observableSource) {
        ObjectHelper.requireNonNull(observableSource, "other is null");
        return RxJavaPlugins.onAssembly(new ObservableSwitchIfEmpty(this, observableSource));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> Observable<R> switchMap(Function<? super T, ? extends ObservableSource<? extends R>> function) {
        return switchMap(function, bufferSize());
    }

    /* JADX WARN: Type inference failed for: r3v0, types: [io.reactivex.functions.Function, io.reactivex.functions.Function<? super T, ? extends io.reactivex.ObservableSource<? extends R>>, java.lang.Object] */
    /* JADX WARNING: Unknown variable types count: 1 */
    @io.reactivex.annotations.CheckReturnValue
    @io.reactivex.annotations.SchedulerSupport(io.reactivex.annotations.SchedulerSupport.NONE)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <R> io.reactivex.Observable<R> switchMap(io.reactivex.functions.Function<? super T, ? extends io.reactivex.ObservableSource<? extends R>> r3, int r4) {
        /*
            r2 = this;
            java.lang.String r0 = "mapper is null"
            io.reactivex.internal.functions.ObjectHelper.requireNonNull(r3, r0)
            java.lang.String r0 = "bufferSize"
            io.reactivex.internal.functions.ObjectHelper.verifyPositive(r4, r0)
            boolean r0 = r2 instanceof io.reactivex.internal.fuseable.ScalarCallable
            if (r0 == 0) goto L_0x0021
            r4 = r2
            io.reactivex.internal.fuseable.ScalarCallable r4 = (io.reactivex.internal.fuseable.ScalarCallable) r4
            java.lang.Object r4 = r4.call()
            if (r4 != 0) goto L_0x001c
            io.reactivex.Observable r3 = empty()
            return r3
        L_0x001c:
            io.reactivex.Observable r3 = io.reactivex.internal.operators.observable.ObservableScalarXMap.scalarXMap(r4, r3)
            return r3
        L_0x0021:
            io.reactivex.internal.operators.observable.ObservableSwitchMap r0 = new io.reactivex.internal.operators.observable.ObservableSwitchMap
            r1 = 0
            r0.<init>(r2, r3, r4, r1)
            io.reactivex.Observable r3 = io.reactivex.plugins.RxJavaPlugins.onAssembly(r0)
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: io.reactivex.Observable.switchMap(io.reactivex.functions.Function, int):io.reactivex.Observable");
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Completable switchMapCompletable(Function<? super T, ? extends CompletableSource> function) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        return RxJavaPlugins.onAssembly(new ObservableSwitchMapCompletable(this, function, false));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Completable switchMapCompletableDelayError(Function<? super T, ? extends CompletableSource> function) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        return RxJavaPlugins.onAssembly(new ObservableSwitchMapCompletable(this, function, true));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> Observable<R> switchMapMaybe(Function<? super T, ? extends MaybeSource<? extends R>> function) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        return RxJavaPlugins.onAssembly(new ObservableSwitchMapMaybe(this, function, false));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> Observable<R> switchMapMaybeDelayError(Function<? super T, ? extends MaybeSource<? extends R>> function) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        return RxJavaPlugins.onAssembly(new ObservableSwitchMapMaybe(this, function, true));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> Observable<R> switchMapSingle(Function<? super T, ? extends SingleSource<? extends R>> function) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        return RxJavaPlugins.onAssembly(new ObservableSwitchMapSingle(this, function, false));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> Observable<R> switchMapSingleDelayError(Function<? super T, ? extends SingleSource<? extends R>> function) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        return RxJavaPlugins.onAssembly(new ObservableSwitchMapSingle(this, function, true));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> Observable<R> switchMapDelayError(Function<? super T, ? extends ObservableSource<? extends R>> function) {
        return switchMapDelayError(function, bufferSize());
    }

    /* JADX WARN: Type inference failed for: r3v0, types: [io.reactivex.functions.Function, io.reactivex.functions.Function<? super T, ? extends io.reactivex.ObservableSource<? extends R>>, java.lang.Object] */
    /* JADX WARNING: Unknown variable types count: 1 */
    @io.reactivex.annotations.CheckReturnValue
    @io.reactivex.annotations.SchedulerSupport(io.reactivex.annotations.SchedulerSupport.NONE)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <R> io.reactivex.Observable<R> switchMapDelayError(io.reactivex.functions.Function<? super T, ? extends io.reactivex.ObservableSource<? extends R>> r3, int r4) {
        /*
            r2 = this;
            java.lang.String r0 = "mapper is null"
            io.reactivex.internal.functions.ObjectHelper.requireNonNull(r3, r0)
            java.lang.String r0 = "bufferSize"
            io.reactivex.internal.functions.ObjectHelper.verifyPositive(r4, r0)
            boolean r0 = r2 instanceof io.reactivex.internal.fuseable.ScalarCallable
            if (r0 == 0) goto L_0x0021
            r4 = r2
            io.reactivex.internal.fuseable.ScalarCallable r4 = (io.reactivex.internal.fuseable.ScalarCallable) r4
            java.lang.Object r4 = r4.call()
            if (r4 != 0) goto L_0x001c
            io.reactivex.Observable r3 = empty()
            return r3
        L_0x001c:
            io.reactivex.Observable r3 = io.reactivex.internal.operators.observable.ObservableScalarXMap.scalarXMap(r4, r3)
            return r3
        L_0x0021:
            io.reactivex.internal.operators.observable.ObservableSwitchMap r0 = new io.reactivex.internal.operators.observable.ObservableSwitchMap
            r1 = 1
            r0.<init>(r2, r3, r4, r1)
            io.reactivex.Observable r3 = io.reactivex.plugins.RxJavaPlugins.onAssembly(r0)
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: io.reactivex.Observable.switchMapDelayError(io.reactivex.functions.Function, int):io.reactivex.Observable");
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> take(long j) {
        if (j >= 0) {
            return RxJavaPlugins.onAssembly(new ObservableTake(this, j));
        }
        throw new IllegalArgumentException("count >= 0 required but it was " + j);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> take(long j, TimeUnit timeUnit) {
        return takeUntil(timer(j, timeUnit));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final Observable<T> take(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return takeUntil(timer(j, timeUnit, scheduler));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> takeLast(int i) {
        if (i < 0) {
            throw new IndexOutOfBoundsException("count >= 0 required but it was " + i);
        } else if (i == 0) {
            return RxJavaPlugins.onAssembly(new ObservableIgnoreElements(this));
        } else {
            if (i == 1) {
                return RxJavaPlugins.onAssembly(new ObservableTakeLastOne(this));
            }
            return RxJavaPlugins.onAssembly(new ObservableTakeLast(this, i));
        }
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.TRAMPOLINE)
    public final Observable<T> takeLast(long j, long j2, TimeUnit timeUnit) {
        return takeLast(j, j2, timeUnit, Schedulers.trampoline(), false, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final Observable<T> takeLast(long j, long j2, TimeUnit timeUnit, Scheduler scheduler) {
        return takeLast(j, j2, timeUnit, scheduler, false, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final Observable<T> takeLast(long j, long j2, TimeUnit timeUnit, Scheduler scheduler, boolean z, int i) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        if (j >= 0) {
            return RxJavaPlugins.onAssembly(new ObservableTakeLastTimed(this, j, j2, timeUnit, scheduler, i, z));
        }
        throw new IndexOutOfBoundsException("count >= 0 required but it was " + j);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.TRAMPOLINE)
    public final Observable<T> takeLast(long j, TimeUnit timeUnit) {
        return takeLast(j, timeUnit, Schedulers.trampoline(), false, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.TRAMPOLINE)
    public final Observable<T> takeLast(long j, TimeUnit timeUnit, boolean z) {
        return takeLast(j, timeUnit, Schedulers.trampoline(), z, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final Observable<T> takeLast(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return takeLast(j, timeUnit, scheduler, false, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final Observable<T> takeLast(long j, TimeUnit timeUnit, Scheduler scheduler, boolean z) {
        return takeLast(j, timeUnit, scheduler, z, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final Observable<T> takeLast(long j, TimeUnit timeUnit, Scheduler scheduler, boolean z, int i) {
        return takeLast(LongCompanionObject.MAX_VALUE, j, timeUnit, scheduler, z, i);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <U> Observable<T> takeUntil(ObservableSource<U> observableSource) {
        ObjectHelper.requireNonNull(observableSource, "other is null");
        return RxJavaPlugins.onAssembly(new ObservableTakeUntil(this, observableSource));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> takeUntil(Predicate<? super T> predicate) {
        ObjectHelper.requireNonNull(predicate, "stopPredicate is null");
        return RxJavaPlugins.onAssembly(new ObservableTakeUntilPredicate(this, predicate));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<T> takeWhile(Predicate<? super T> predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly(new ObservableTakeWhile(this, predicate));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.COMPUTATION)
    public final Observable<T> throttleFirst(long j, TimeUnit timeUnit) {
        return throttleFirst(j, timeUnit, Schedulers.computation());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final Observable<T> throttleFirst(long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new ObservableThrottleFirstTimed(this, j, timeUnit, scheduler));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.COMPUTATION)
    public final Observable<T> throttleLast(long j, TimeUnit timeUnit) {
        return sample(j, timeUnit);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final Observable<T> throttleLast(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return sample(j, timeUnit, scheduler);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.COMPUTATION)
    public final Observable<T> throttleLatest(long j, TimeUnit timeUnit) {
        return throttleLatest(j, timeUnit, Schedulers.computation(), false);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.COMPUTATION)
    public final Observable<T> throttleLatest(long j, TimeUnit timeUnit, boolean z) {
        return throttleLatest(j, timeUnit, Schedulers.computation(), z);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final Observable<T> throttleLatest(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return throttleLatest(j, timeUnit, scheduler, false);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final Observable<T> throttleLatest(long j, TimeUnit timeUnit, Scheduler scheduler, boolean z) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new ObservableThrottleLatest(this, j, timeUnit, scheduler, z));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.COMPUTATION)
    public final Observable<T> throttleWithTimeout(long j, TimeUnit timeUnit) {
        return debounce(j, timeUnit);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final Observable<T> throttleWithTimeout(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return debounce(j, timeUnit, scheduler);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<Timed<T>> timeInterval() {
        return timeInterval(TimeUnit.MILLISECONDS, Schedulers.computation());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<Timed<T>> timeInterval(Scheduler scheduler) {
        return timeInterval(TimeUnit.MILLISECONDS, scheduler);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<Timed<T>> timeInterval(TimeUnit timeUnit) {
        return timeInterval(timeUnit, Schedulers.computation());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<Timed<T>> timeInterval(TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new ObservableTimeInterval(this, timeUnit, scheduler));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <V> Observable<T> timeout(Function<? super T, ? extends ObservableSource<V>> function) {
        return timeout0(null, function, null);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <V> Observable<T> timeout(Function<? super T, ? extends ObservableSource<V>> function, ObservableSource<? extends T> observableSource) {
        ObjectHelper.requireNonNull(observableSource, "other is null");
        return timeout0(null, function, observableSource);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.COMPUTATION)
    public final Observable<T> timeout(long j, TimeUnit timeUnit) {
        return timeout0(j, timeUnit, null, Schedulers.computation());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.COMPUTATION)
    public final Observable<T> timeout(long j, TimeUnit timeUnit, ObservableSource<? extends T> observableSource) {
        ObjectHelper.requireNonNull(observableSource, "other is null");
        return timeout0(j, timeUnit, observableSource, Schedulers.computation());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final Observable<T> timeout(long j, TimeUnit timeUnit, Scheduler scheduler, ObservableSource<? extends T> observableSource) {
        ObjectHelper.requireNonNull(observableSource, "other is null");
        return timeout0(j, timeUnit, observableSource, scheduler);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final Observable<T> timeout(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return timeout0(j, timeUnit, null, scheduler);
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [io.reactivex.ObservableSource, java.lang.Object, io.reactivex.ObservableSource<U>] */
    /* JADX WARNING: Unknown variable types count: 1 */
    @io.reactivex.annotations.CheckReturnValue
    @io.reactivex.annotations.SchedulerSupport(io.reactivex.annotations.SchedulerSupport.NONE)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <U, V> io.reactivex.Observable<T> timeout(io.reactivex.ObservableSource<U> r2, io.reactivex.functions.Function<? super T, ? extends io.reactivex.ObservableSource<V>> r3) {
        /*
            r1 = this;
            java.lang.String r0 = "firstTimeoutIndicator is null"
            io.reactivex.internal.functions.ObjectHelper.requireNonNull(r2, r0)
            r0 = 0
            io.reactivex.Observable r2 = r1.timeout0(r2, r3, r0)
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: io.reactivex.Observable.timeout(io.reactivex.ObservableSource, io.reactivex.functions.Function):io.reactivex.Observable");
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [io.reactivex.ObservableSource, java.lang.Object, io.reactivex.ObservableSource<U>] */
    /* JADX WARNING: Unknown variable types count: 1 */
    @io.reactivex.annotations.CheckReturnValue
    @io.reactivex.annotations.SchedulerSupport(io.reactivex.annotations.SchedulerSupport.NONE)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <U, V> io.reactivex.Observable<T> timeout(io.reactivex.ObservableSource<U> r2, io.reactivex.functions.Function<? super T, ? extends io.reactivex.ObservableSource<V>> r3, io.reactivex.ObservableSource<? extends T> r4) {
        /*
            r1 = this;
            java.lang.String r0 = "firstTimeoutIndicator is null"
            io.reactivex.internal.functions.ObjectHelper.requireNonNull(r2, r0)
            java.lang.String r0 = "other is null"
            io.reactivex.internal.functions.ObjectHelper.requireNonNull(r4, r0)
            io.reactivex.Observable r2 = r1.timeout0(r2, r3, r4)
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: io.reactivex.Observable.timeout(io.reactivex.ObservableSource, io.reactivex.functions.Function, io.reactivex.ObservableSource):io.reactivex.Observable");
    }

    private Observable<T> timeout0(long j, TimeUnit timeUnit, ObservableSource<? extends T> observableSource, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "timeUnit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new ObservableTimeoutTimed(this, j, timeUnit, scheduler, observableSource));
    }

    private <U, V> Observable<T> timeout0(ObservableSource<U> observableSource, Function<? super T, ? extends ObservableSource<V>> function, ObservableSource<? extends T> observableSource2) {
        ObjectHelper.requireNonNull(function, "itemTimeoutIndicator is null");
        return RxJavaPlugins.onAssembly(new ObservableTimeout(this, observableSource, function, observableSource2));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<Timed<T>> timestamp() {
        return timestamp(TimeUnit.MILLISECONDS, Schedulers.computation());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<Timed<T>> timestamp(Scheduler scheduler) {
        return timestamp(TimeUnit.MILLISECONDS, scheduler);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<Timed<T>> timestamp(TimeUnit timeUnit) {
        return timestamp(timeUnit, Schedulers.computation());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<Timed<T>> timestamp(TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return map(Functions.timestampWith(timeUnit, scheduler));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> R to(Function<? super Observable<T>, R> function) {
        try {
            return ((Function) ObjectHelper.requireNonNull(function, "converter is null")).apply(this);
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            throw ExceptionHelper.wrapOrThrow(th);
        }
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Single<List<T>> toList() {
        return toList(16);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Single<List<T>> toList(int i) {
        ObjectHelper.verifyPositive(i, "capacityHint");
        return RxJavaPlugins.onAssembly(new ObservableToListSingle(this, i));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <U extends Collection<? super T>> Single<U> toList(Callable<U> callable) {
        ObjectHelper.requireNonNull(callable, "collectionSupplier is null");
        return RxJavaPlugins.onAssembly(new ObservableToListSingle(this, callable));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <K> Single<Map<K, T>> toMap(Function<? super T, ? extends K> function) {
        ObjectHelper.requireNonNull(function, "keySelector is null");
        return collect(HashMapSupplier.asCallable(), Functions.toMapKeySelector(function));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <K, V> Single<Map<K, V>> toMap(Function<? super T, ? extends K> function, Function<? super T, ? extends V> function2) {
        ObjectHelper.requireNonNull(function, "keySelector is null");
        ObjectHelper.requireNonNull(function2, "valueSelector is null");
        return collect(HashMapSupplier.asCallable(), Functions.toMapKeyValueSelector(function, function2));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <K, V> Single<Map<K, V>> toMap(Function<? super T, ? extends K> function, Function<? super T, ? extends V> function2, Callable<? extends Map<K, V>> callable) {
        ObjectHelper.requireNonNull(function, "keySelector is null");
        ObjectHelper.requireNonNull(function2, "valueSelector is null");
        ObjectHelper.requireNonNull(callable, "mapSupplier is null");
        return collect(callable, Functions.toMapKeyValueSelector(function, function2));
    }

    /* JADX WARN: Type inference failed for: r4v0, types: [io.reactivex.functions.Function, io.reactivex.functions.Function<? super T, ? extends K>] */
    /* JADX WARNING: Unknown variable types count: 1 */
    @io.reactivex.annotations.CheckReturnValue
    @io.reactivex.annotations.SchedulerSupport(io.reactivex.annotations.SchedulerSupport.NONE)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <K> io.reactivex.Single<java.util.Map<K, java.util.Collection<T>>> toMultimap(io.reactivex.functions.Function<? super T, ? extends K> r4) {
        /*
            r3 = this;
            io.reactivex.functions.Function r0 = io.reactivex.internal.functions.Functions.identity()
            java.util.concurrent.Callable r1 = io.reactivex.internal.util.HashMapSupplier.asCallable()
            io.reactivex.functions.Function r2 = io.reactivex.internal.util.ArrayListSupplier.asFunction()
            io.reactivex.Single r4 = r3.toMultimap(r4, r0, r1, r2)
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: io.reactivex.Observable.toMultimap(io.reactivex.functions.Function):io.reactivex.Single");
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <K, V> Single<Map<K, Collection<V>>> toMultimap(Function<? super T, ? extends K> function, Function<? super T, ? extends V> function2) {
        return toMultimap(function, function2, HashMapSupplier.asCallable(), ArrayListSupplier.asFunction());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <K, V> Single<Map<K, Collection<V>>> toMultimap(Function<? super T, ? extends K> function, Function<? super T, ? extends V> function2, Callable<? extends Map<K, Collection<V>>> callable, Function<? super K, ? extends Collection<? super V>> function3) {
        ObjectHelper.requireNonNull(function, "keySelector is null");
        ObjectHelper.requireNonNull(function2, "valueSelector is null");
        ObjectHelper.requireNonNull(callable, "mapSupplier is null");
        ObjectHelper.requireNonNull(function3, "collectionFactory is null");
        return collect(callable, Functions.toMultimapKeyValueSelector(function, function2, function3));
    }

    /* JADX WARN: Type inference failed for: r4v0, types: [java.util.concurrent.Callable, java.util.concurrent.Callable<java.util.Map<K, java.util.Collection<V>>>] */
    /* JADX WARNING: Unknown variable types count: 1 */
    @io.reactivex.annotations.CheckReturnValue
    @io.reactivex.annotations.SchedulerSupport(io.reactivex.annotations.SchedulerSupport.NONE)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <K, V> io.reactivex.Single<java.util.Map<K, java.util.Collection<V>>> toMultimap(io.reactivex.functions.Function<? super T, ? extends K> r2, io.reactivex.functions.Function<? super T, ? extends V> r3, java.util.concurrent.Callable<java.util.Map<K, java.util.Collection<V>>> r4) {
        /*
            r1 = this;
            io.reactivex.functions.Function r0 = io.reactivex.internal.util.ArrayListSupplier.asFunction()
            io.reactivex.Single r2 = r1.toMultimap(r2, r3, r4, r0)
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: io.reactivex.Observable.toMultimap(io.reactivex.functions.Function, io.reactivex.functions.Function, java.util.concurrent.Callable):io.reactivex.Single");
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Flowable<T> toFlowable(BackpressureStrategy backpressureStrategy) {
        FlowableFromObservable flowableFromObservable = new FlowableFromObservable(this);
        int i = AnonymousClass1.$SwitchMap$io$reactivex$BackpressureStrategy[backpressureStrategy.ordinal()];
        if (i == 1) {
            return flowableFromObservable.onBackpressureDrop();
        }
        if (i == 2) {
            return flowableFromObservable.onBackpressureLatest();
        }
        if (i == 3) {
            return flowableFromObservable;
        }
        if (i != 4) {
            return flowableFromObservable.onBackpressureBuffer();
        }
        return RxJavaPlugins.onAssembly(new FlowableOnBackpressureError(flowableFromObservable));
    }

    /* renamed from: io.reactivex.Observable$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$io$reactivex$BackpressureStrategy;

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        static {
            /*
                io.reactivex.BackpressureStrategy[] r0 = io.reactivex.BackpressureStrategy.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                io.reactivex.Observable.AnonymousClass1.$SwitchMap$io$reactivex$BackpressureStrategy = r0
                io.reactivex.BackpressureStrategy r1 = io.reactivex.BackpressureStrategy.DROP     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = io.reactivex.Observable.AnonymousClass1.$SwitchMap$io$reactivex$BackpressureStrategy     // Catch:{ NoSuchFieldError -> 0x001d }
                io.reactivex.BackpressureStrategy r1 = io.reactivex.BackpressureStrategy.LATEST     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = io.reactivex.Observable.AnonymousClass1.$SwitchMap$io$reactivex$BackpressureStrategy     // Catch:{ NoSuchFieldError -> 0x0028 }
                io.reactivex.BackpressureStrategy r1 = io.reactivex.BackpressureStrategy.MISSING     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = io.reactivex.Observable.AnonymousClass1.$SwitchMap$io$reactivex$BackpressureStrategy     // Catch:{ NoSuchFieldError -> 0x0033 }
                io.reactivex.BackpressureStrategy r1 = io.reactivex.BackpressureStrategy.ERROR     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: io.reactivex.Observable.AnonymousClass1.<clinit>():void");
        }
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Single<List<T>> toSortedList() {
        return toSortedList(Functions.naturalOrder());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Single<List<T>> toSortedList(Comparator<? super T> comparator) {
        ObjectHelper.requireNonNull(comparator, "comparator is null");
        return toList().map(Functions.listSorter(comparator));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Single<List<T>> toSortedList(Comparator<? super T> comparator, int i) {
        ObjectHelper.requireNonNull(comparator, "comparator is null");
        return toList(i).map(Functions.listSorter(comparator));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Single<List<T>> toSortedList(int i) {
        return toSortedList(Functions.naturalOrder(), i);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final Observable<T> unsubscribeOn(Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new ObservableUnsubscribeOn(this, scheduler));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<Observable<T>> window(long j) {
        return window(j, j, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<Observable<T>> window(long j, long j2) {
        return window(j, j2, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Observable<Observable<T>> window(long j, long j2, int i) {
        ObjectHelper.verifyPositive(j, "count");
        ObjectHelper.verifyPositive(j2, "skip");
        ObjectHelper.verifyPositive(i, "bufferSize");
        return RxJavaPlugins.onAssembly(new ObservableWindow(this, j, j2, i));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.COMPUTATION)
    public final Observable<Observable<T>> window(long j, long j2, TimeUnit timeUnit) {
        return window(j, j2, timeUnit, Schedulers.computation(), bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final Observable<Observable<T>> window(long j, long j2, TimeUnit timeUnit, Scheduler scheduler) {
        return window(j, j2, timeUnit, scheduler, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final Observable<Observable<T>> window(long j, long j2, TimeUnit timeUnit, Scheduler scheduler, int i) {
        ObjectHelper.verifyPositive(j, "timespan");
        long j3 = j2;
        ObjectHelper.verifyPositive(j3, "timeskip");
        int i2 = i;
        ObjectHelper.verifyPositive(i2, "bufferSize");
        Scheduler scheduler2 = scheduler;
        ObjectHelper.requireNonNull(scheduler2, "scheduler is null");
        TimeUnit timeUnit2 = timeUnit;
        ObjectHelper.requireNonNull(timeUnit2, "unit is null");
        return RxJavaPlugins.onAssembly(new ObservableWindowTimed(this, j, j3, timeUnit2, scheduler2, LongCompanionObject.MAX_VALUE, i2, false));
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: io.reactivex.Observable.window(long, java.util.concurrent.TimeUnit, io.reactivex.Scheduler, long, boolean):io.reactivex.Observable<io.reactivex.Observable<T>>
     arg types: [long, java.util.concurrent.TimeUnit, io.reactivex.Scheduler, ?, int]
     candidates:
      io.reactivex.Observable.window(long, long, java.util.concurrent.TimeUnit, io.reactivex.Scheduler, int):io.reactivex.Observable<io.reactivex.Observable<T>>
      io.reactivex.Observable.window(long, java.util.concurrent.TimeUnit, io.reactivex.Scheduler, long, boolean):io.reactivex.Observable<io.reactivex.Observable<T>> */
    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.COMPUTATION)
    public final Observable<Observable<T>> window(long j, TimeUnit timeUnit) {
        return window(j, timeUnit, Schedulers.computation(), (long) LongCompanionObject.MAX_VALUE, false);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: io.reactivex.Observable.window(long, java.util.concurrent.TimeUnit, io.reactivex.Scheduler, long, boolean):io.reactivex.Observable<io.reactivex.Observable<T>>
     arg types: [long, java.util.concurrent.TimeUnit, io.reactivex.Scheduler, long, int]
     candidates:
      io.reactivex.Observable.window(long, long, java.util.concurrent.TimeUnit, io.reactivex.Scheduler, int):io.reactivex.Observable<io.reactivex.Observable<T>>
      io.reactivex.Observable.window(long, java.util.concurrent.TimeUnit, io.reactivex.Scheduler, long, boolean):io.reactivex.Observable<io.reactivex.Observable<T>> */
    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.COMPUTATION)
    public final Observable<Observable<T>> window(long j, TimeUnit timeUnit, long j2) {
        return window(j, timeUnit, Schedulers.computation(), j2, false);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.COMPUTATION)
    public final Observable<Observable<T>> window(long j, TimeUnit timeUnit, long j2, boolean z) {
        return window(j, timeUnit, Schedulers.computation(), j2, z);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: io.reactivex.Observable.window(long, java.util.concurrent.TimeUnit, io.reactivex.Scheduler, long, boolean):io.reactivex.Observable<io.reactivex.Observable<T>>
     arg types: [long, java.util.concurrent.TimeUnit, io.reactivex.Scheduler, ?, int]
     candidates:
      io.reactivex.Observable.window(long, long, java.util.concurrent.TimeUnit, io.reactivex.Scheduler, int):io.reactivex.Observable<io.reactivex.Observable<T>>
      io.reactivex.Observable.window(long, java.util.concurrent.TimeUnit, io.reactivex.Scheduler, long, boolean):io.reactivex.Observable<io.reactivex.Observable<T>> */
    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final Observable<Observable<T>> window(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return window(j, timeUnit, scheduler, (long) LongCompanionObject.MAX_VALUE, false);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: io.reactivex.Observable.window(long, java.util.concurrent.TimeUnit, io.reactivex.Scheduler, long, boolean):io.reactivex.Observable<io.reactivex.Observable<T>>
     arg types: [long, java.util.concurrent.TimeUnit, io.reactivex.Scheduler, long, int]
     candidates:
      io.reactivex.Observable.window(long, long, java.util.concurrent.TimeUnit, io.reactivex.Scheduler, int):io.reactivex.Observable<io.reactivex.Observable<T>>
      io.reactivex.Observable.window(long, java.util.concurrent.TimeUnit, io.reactivex.Scheduler, long, boolean):io.reactivex.Observable<io.reactivex.Observable<T>> */
    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final Observable<Observable<T>> window(long j, TimeUnit timeUnit, Scheduler scheduler, long j2) {
        return window(j, timeUnit, scheduler, j2, false);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final Observable<Observable<T>> window(long j, TimeUnit timeUnit, Scheduler scheduler, long j2, boolean z) {
        return window(j, timeUnit, scheduler, j2, z, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final Observable<Observable<T>> window(long j, TimeUnit timeUnit, Scheduler scheduler, long j2, boolean z, int i) {
        int i2 = i;
        ObjectHelper.verifyPositive(i2, "bufferSize");
        Scheduler scheduler2 = scheduler;
        ObjectHelper.requireNonNull(scheduler2, "scheduler is null");
        TimeUnit timeUnit2 = timeUnit;
        ObjectHelper.requireNonNull(timeUnit2, "unit is null");
        long j3 = j2;
        ObjectHelper.verifyPositive(j3, "count");
        return RxJavaPlugins.onAssembly(new ObservableWindowTimed(this, j, j, timeUnit2, scheduler2, j3, i2, z));
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [io.reactivex.ObservableSource<B>, io.reactivex.ObservableSource] */
    /* JADX WARNING: Unknown variable types count: 1 */
    @io.reactivex.annotations.CheckReturnValue
    @io.reactivex.annotations.SchedulerSupport(io.reactivex.annotations.SchedulerSupport.NONE)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <B> io.reactivex.Observable<io.reactivex.Observable<T>> window(io.reactivex.ObservableSource<B> r2) {
        /*
            r1 = this;
            int r0 = bufferSize()
            io.reactivex.Observable r2 = r1.window(r2, r0)
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: io.reactivex.Observable.window(io.reactivex.ObservableSource):io.reactivex.Observable");
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <B> Observable<Observable<T>> window(ObservableSource<B> observableSource, int i) {
        ObjectHelper.requireNonNull(observableSource, "boundary is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        return RxJavaPlugins.onAssembly(new ObservableWindowBoundary(this, observableSource, i));
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [io.reactivex.ObservableSource, io.reactivex.ObservableSource<U>] */
    /* JADX WARNING: Unknown variable types count: 1 */
    @io.reactivex.annotations.CheckReturnValue
    @io.reactivex.annotations.SchedulerSupport(io.reactivex.annotations.SchedulerSupport.NONE)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <U, V> io.reactivex.Observable<io.reactivex.Observable<T>> window(io.reactivex.ObservableSource<U> r2, io.reactivex.functions.Function<? super U, ? extends io.reactivex.ObservableSource<V>> r3) {
        /*
            r1 = this;
            int r0 = bufferSize()
            io.reactivex.Observable r2 = r1.window(r2, r3, r0)
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: io.reactivex.Observable.window(io.reactivex.ObservableSource, io.reactivex.functions.Function):io.reactivex.Observable");
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <U, V> Observable<Observable<T>> window(ObservableSource<U> observableSource, Function<? super U, ? extends ObservableSource<V>> function, int i) {
        ObjectHelper.requireNonNull(observableSource, "openingIndicator is null");
        ObjectHelper.requireNonNull(function, "closingIndicator is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        return RxJavaPlugins.onAssembly(new ObservableWindowBoundarySelector(this, observableSource, function, i));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <B> Observable<Observable<T>> window(Callable<? extends ObservableSource<B>> callable) {
        return window(callable, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <B> Observable<Observable<T>> window(Callable<? extends ObservableSource<B>> callable, int i) {
        ObjectHelper.requireNonNull(callable, "boundary is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        return RxJavaPlugins.onAssembly(new ObservableWindowBoundarySupplier(this, callable, i));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <U, R> Observable<R> withLatestFrom(ObservableSource<? extends U> observableSource, BiFunction<? super T, ? super U, ? extends R> biFunction) {
        ObjectHelper.requireNonNull(observableSource, "other is null");
        ObjectHelper.requireNonNull(biFunction, "combiner is null");
        return RxJavaPlugins.onAssembly(new ObservableWithLatestFrom(this, biFunction, observableSource));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <T1, T2, R> Observable<R> withLatestFrom(ObservableSource<T1> observableSource, ObservableSource<T2> observableSource2, Function3<? super T, ? super T1, ? super T2, R> function3) {
        ObjectHelper.requireNonNull(observableSource, "o1 is null");
        ObjectHelper.requireNonNull(observableSource2, "o2 is null");
        ObjectHelper.requireNonNull(function3, "combiner is null");
        return withLatestFrom(new ObservableSource[]{observableSource, observableSource2}, Functions.toFunction(function3));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <T1, T2, T3, R> Observable<R> withLatestFrom(ObservableSource<T1> observableSource, ObservableSource<T2> observableSource2, ObservableSource<T3> observableSource3, Function4<? super T, ? super T1, ? super T2, ? super T3, R> function4) {
        ObjectHelper.requireNonNull(observableSource, "o1 is null");
        ObjectHelper.requireNonNull(observableSource2, "o2 is null");
        ObjectHelper.requireNonNull(observableSource3, "o3 is null");
        ObjectHelper.requireNonNull(function4, "combiner is null");
        return withLatestFrom(new ObservableSource[]{observableSource, observableSource2, observableSource3}, Functions.toFunction(function4));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <T1, T2, T3, T4, R> Observable<R> withLatestFrom(ObservableSource<T1> observableSource, ObservableSource<T2> observableSource2, ObservableSource<T3> observableSource3, ObservableSource<T4> observableSource4, Function5<? super T, ? super T1, ? super T2, ? super T3, ? super T4, R> function5) {
        ObjectHelper.requireNonNull(observableSource, "o1 is null");
        ObjectHelper.requireNonNull(observableSource2, "o2 is null");
        ObjectHelper.requireNonNull(observableSource3, "o3 is null");
        ObjectHelper.requireNonNull(observableSource4, "o4 is null");
        ObjectHelper.requireNonNull(function5, "combiner is null");
        return withLatestFrom(new ObservableSource[]{observableSource, observableSource2, observableSource3, observableSource4}, Functions.toFunction(function5));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> Observable<R> withLatestFrom(ObservableSource<?>[] observableSourceArr, Function<? super Object[], R> function) {
        ObjectHelper.requireNonNull(observableSourceArr, "others is null");
        ObjectHelper.requireNonNull(function, "combiner is null");
        return RxJavaPlugins.onAssembly(new ObservableWithLatestFromMany(this, observableSourceArr, function));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> Observable<R> withLatestFrom(Iterable<? extends ObservableSource<?>> iterable, Function<? super Object[], R> function) {
        ObjectHelper.requireNonNull(iterable, "others is null");
        ObjectHelper.requireNonNull(function, "combiner is null");
        return RxJavaPlugins.onAssembly(new ObservableWithLatestFromMany(this, iterable, function));
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <U, R> Observable<R> zipWith(Iterable<U> iterable, BiFunction<? super T, ? super U, ? extends R> biFunction) {
        ObjectHelper.requireNonNull(iterable, "other is null");
        ObjectHelper.requireNonNull(biFunction, "zipper is null");
        return RxJavaPlugins.onAssembly(new ObservableZipIterable(this, iterable, biFunction));
    }

    /* JADX WARN: Type inference failed for: r3v0, types: [io.reactivex.functions.BiFunction, io.reactivex.functions.BiFunction<? super T, ? super U, ? extends R>] */
    /* JADX WARNING: Unknown variable types count: 1 */
    @io.reactivex.annotations.CheckReturnValue
    @io.reactivex.annotations.SchedulerSupport(io.reactivex.annotations.SchedulerSupport.NONE)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <U, R> io.reactivex.Observable<R> zipWith(io.reactivex.ObservableSource<? extends U> r2, io.reactivex.functions.BiFunction<? super T, ? super U, ? extends R> r3) {
        /*
            r1 = this;
            java.lang.String r0 = "other is null"
            io.reactivex.internal.functions.ObjectHelper.requireNonNull(r2, r0)
            io.reactivex.Observable r2 = zip(r1, r2, r3)
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: io.reactivex.Observable.zipWith(io.reactivex.ObservableSource, io.reactivex.functions.BiFunction):io.reactivex.Observable");
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [io.reactivex.functions.BiFunction, io.reactivex.functions.BiFunction<? super T, ? super U, ? extends R>] */
    /* JADX WARNING: Unknown variable types count: 1 */
    @io.reactivex.annotations.CheckReturnValue
    @io.reactivex.annotations.SchedulerSupport(io.reactivex.annotations.SchedulerSupport.NONE)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <U, R> io.reactivex.Observable<R> zipWith(io.reactivex.ObservableSource<? extends U> r1, io.reactivex.functions.BiFunction<? super T, ? super U, ? extends R> r2, boolean r3) {
        /*
            r0 = this;
            io.reactivex.Observable r1 = zip(r0, r1, r2, r3)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: io.reactivex.Observable.zipWith(io.reactivex.ObservableSource, io.reactivex.functions.BiFunction, boolean):io.reactivex.Observable");
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [io.reactivex.functions.BiFunction, io.reactivex.functions.BiFunction<? super T, ? super U, ? extends R>] */
    /* JADX WARNING: Unknown variable types count: 1 */
    @io.reactivex.annotations.CheckReturnValue
    @io.reactivex.annotations.SchedulerSupport(io.reactivex.annotations.SchedulerSupport.NONE)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <U, R> io.reactivex.Observable<R> zipWith(io.reactivex.ObservableSource<? extends U> r1, io.reactivex.functions.BiFunction<? super T, ? super U, ? extends R> r2, boolean r3, int r4) {
        /*
            r0 = this;
            io.reactivex.Observable r1 = zip(r0, r1, r2, r3, r4)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: io.reactivex.Observable.zipWith(io.reactivex.ObservableSource, io.reactivex.functions.BiFunction, boolean, int):io.reactivex.Observable");
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final TestObserver<T> test() {
        TestObserver<T> testObserver = new TestObserver<>();
        subscribe(testObserver);
        return testObserver;
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final TestObserver<T> test(boolean z) {
        TestObserver<T> testObserver = new TestObserver<>();
        if (z) {
            testObserver.dispose();
        }
        subscribe(testObserver);
        return testObserver;
    }
}