package com.immortals.DSA;

import java.util.*;
import java.util.function.LongPredicate;

public class BinarySearch {

    /** Classic closed-interval template. Returns an index of target, or -1. O(log n). */
    static int binarySearch(int[] a, int target) {
        int lo = 0, hi = a.length - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;          // never (lo + hi) / 2 -- it overflows
            if (a[mid] == target) return mid;
            if (a[mid] < target) lo = mid + 1;
            else hi = mid - 1;
        }
        return -1;
    }

    /** First index i with a[i] >= target (a.length if none). Half-open [lo, hi) template. */
    static int lowerBound(int[] a, int target) {
        int lo = 0, hi = a.length;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (a[mid] < target) lo = mid + 1;     // everything <= mid is too small
            else hi = mid;                         // mid might be the answer -- keep it
        }
        return lo;
    }

    /** First index i with a[i] > target (a.length if none). */
    static int upperBound(int[] a, int target) {
        int lo = 0, hi = a.length;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (a[mid] <= target) lo = mid + 1;
            else hi = mid;
        }
        return lo;
    }

    static int firstOccurrence(int[] a, int target) {
        int i = lowerBound(a, target);
        return (i < a.length && a[i] == target) ? i : -1;
    }

    static int lastOccurrence(int[] a, int target) {
        int i = upperBound(a, target) - 1;
        return (i >= 0 && a[i] == target) ? i : -1;
    }

    static int countOccurrences(int[] a, int target) {
        return upperBound(a, target) - lowerBound(a, target);
    }

    /** Index of the largest element <= target, or -1. */
    static int floorIndex(int[] a, int target) {
        return upperBound(a, target) - 1;
    }

    /** Index of the smallest element >= target, or -1. */
    static int ceilIndex(int[] a, int target) {
        int i = lowerBound(a, target);
        return i < a.length ? i : -1;
    }

    /** Smallest x in [lo, hi) with pred(x) true, for a monotone pred (F..F T..T). Returns hi if none. */
    static long firstTrue(long lo, long hi, LongPredicate pred) {
        while (lo < hi) {
            long mid = lo + (hi - lo) / 2;
            if (pred.test(mid)) hi = mid;
            else lo = mid + 1;
        }
        return lo;
    }

    /** LeetCode 278 -- versions 1..n, isBad is monotone. */
    static int firstBadVersion(int n, LongPredicate isBad) {
        return (int) firstTrue(1, (long) n + 1, isBad);
    }

    /** LeetCode 69 -- largest r with r*r <= x, as "first r whose square exceeds x", minus one. */
    static int mySqrt(int x) {
        return (int) (firstTrue(0, (long) x + 2, r -> r * r > x) - 1);   // long math: r*r can pass 2^31
    }

    /** LeetCode 74 -- rows sorted, each row starts above the previous row's last element. */
    static boolean searchMatrix(int[][] matrix, int target) {
        int m = matrix.length, n = matrix[0].length;
        int lo = 0, hi = m * n - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            int v = matrix[mid / n][mid % n];      // map the flat index back to (row, col)
            if (v == target) return true;
            if (v < target) lo = mid + 1;
            else hi = mid - 1;
        }
        return false;
    }

    /** LeetCode 1539 -- arr[i] - (i + 1) counts positives missing before arr[i]; it never decreases. */
    static int kthMissing(int[] arr, int k) {
        int lo = 0, hi = arr.length;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (arr[mid] - (mid + 1) < k) lo = mid + 1;
            else hi = mid;
        }
        return lo + k;
    }

    /** Real-valued search: a fixed number of halvings, never a floating-point equality test. */
    static double realSqrt(double x) {
        double lo = 0.0, hi = Math.max(1.0, x);
        for (int i = 0; i < 100; i++) {
            double mid = (lo + hi) / 2;
            if (mid * mid < x) lo = mid;
            else hi = mid;
        }
        return (lo + hi) / 2;
    }

    public static void main(String[] args) {
        int[] a = {1, 2, 4, 4, 4, 7, 9};
        System.out.println("binary_search(7):        " + binarySearch(a, 7));
        // Expected: 5
        System.out.println("binary_search(3):        " + binarySearch(a, 3));
        // Expected: -1
        System.out.println("lower_bound(4):          " + lowerBound(a, 4));
        // Expected: 2
        System.out.println("upper_bound(4):          " + upperBound(a, 4));
        // Expected: 5
        System.out.println("count_occurrences(4):    " + countOccurrences(a, 4));
        // Expected: 3
        System.out.println("first / last occurrence: " + firstOccurrence(a, 4) + " " + lastOccurrence(a, 4));
        // Expected: 2 4
        System.out.println("insert positions 5,0,10: " + lowerBound(a, 5) + " " + lowerBound(a, 0) + " " + lowerBound(a, 10));
        // Expected: 5 0 7
        System.out.println("floor(8), ceil(8):       " + a[floorIndex(a, 8)] + " " + a[ceilIndex(a, 8)]);
        // Expected: 7 9
        System.out.println("floor(0):                " + floorIndex(a, 0));
        // Expected: -1
        System.out.println("my_sqrt(8):              " + mySqrt(8));
        // Expected: 2
        System.out.println("my_sqrt(2147395599):     " + mySqrt(2147395599));
        // Expected: 46339
        System.out.println("first_bad_version(10):   " + firstBadVersion(10, v -> v >= 4));
        // Expected: 4
        int[][] grid = {{1, 3, 5, 7}, {10, 11, 16, 20}, {23, 30, 34, 60}};
        System.out.println("search_matrix 3, 13:     " + searchMatrix(grid, 3) + " " + searchMatrix(grid, 13));
        // Expected: true false
        System.out.println("kth_missing:             " + kthMissing(new int[]{2, 3, 4, 7, 11}, 5) + " " + kthMissing(new int[]{1, 2, 3, 4}, 2));
        // Expected: 9 6
        System.out.println("real_sqrt(2):            " + String.format("%.6f", realSqrt(2.0)));
        // Expected: 1.414214

        int lo = 1_500_000_000, hi = 2_000_000_000;
        System.out.println("lo + hi as int32:        " + (lo + hi));
        // Expected: -794967296
        System.out.println("(lo + hi) / 2 (buggy):   " + (lo + hi) / 2);
        // Expected: -397483648
        System.out.println("lo + (hi - lo) / 2:      " + (lo + (hi - lo) / 2));
        // Expected: 1750000000
        System.out.println("(lo + hi) >>> 1:         " + ((lo + hi) >>> 1));
        // Expected: 1750000000

        Random rng = new Random(7);
        for (int trial = 0; trial < 2000; trial++) {
            int[] arr = new int[rng.nextInt(16)];
            for (int i = 0; i < arr.length; i++) arr[i] = rng.nextInt(21);
            Arrays.sort(arr);
            int t = rng.nextInt(23) - 1;
            int lb = 0, ub = 0;
            for (int x : arr) { if (x < t) lb++; if (x <= t) ub++; }
            if (lowerBound(arr, t) != lb || upperBound(arr, t) != ub) throw new AssertionError("bounds");
            int idx = binarySearch(arr, t);
            boolean present = ub > lb;
            if ((idx != -1) != present || (idx != -1 && arr[idx] != t)) throw new AssertionError("search");
        }
        System.out.println("randomized check passed: 2000 arrays");
    }
}