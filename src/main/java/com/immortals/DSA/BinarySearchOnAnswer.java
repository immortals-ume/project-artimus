package com.immortals.DSA;

import java.util.*;
import java.util.function.LongPredicate;

public class BinarySearchOnAnswer {

    /** Smallest x in [lo, hi] with feasible(x) true -- MINIMISE. hi must itself be feasible. */
    static long minFeasible(long lo, long hi, LongPredicate feasible) {
        while (lo < hi) {
            long mid = lo + (hi - lo) / 2;
            if (feasible.test(mid)) hi = mid;         // mid works: try smaller, keep mid
            else lo = mid + 1;                        // mid fails: everything <= mid fails too
        }
        return lo;
    }

    /** Largest x in [lo, hi] with feasible(x) true -- MAXIMISE. lo must itself be feasible. */
    static long maxFeasible(long lo, long hi, LongPredicate feasible) {
        while (lo < hi) {
            long mid = lo + (hi - lo + 1) / 2;        // UPPER middle: with the lower one, lo = mid never advances
            if (feasible.test(mid)) lo = mid;         // mid works: try larger, keep mid
            else hi = mid - 1;                        // mid fails: everything >= mid fails too
        }
        return lo;
    }

    /** LeetCode 875 -- smallest speed k with sum(ceil(p / k)) <= h. */
    static int kokoEatingBananas(int[] piles, int h) {
        long hi = Arrays.stream(piles).max().getAsInt();
        return (int) minFeasible(1, hi, k -> {
            long hours = 0;
            for (int p : piles) hours += (p + k - 1) / k;
            return hours <= h;
        });
    }

    /** Greedy: fewest contiguous groups whose sums stay <= cap (requires cap >= max(items)). */
    static int groupsNeeded(int[] items, long cap) {
        int groups = 1;
        long load = 0;
        for (int x : items) {
            if (load + x > cap) { groups++; load = 0; }
            load += x;
        }
        return groups;
    }

    /** LeetCode 1011 -- smallest capacity that ships every package, in order, within `days`. */
    static int shipWithinDays(int[] weights, int days) {
        long lo = Arrays.stream(weights).max().getAsInt();
        long hi = Arrays.stream(weights).asLongStream().sum();
        return (int) minFeasible(lo, hi, cap -> groupsNeeded(weights, cap) <= days);
    }

    /** LeetCode 410 -- the SAME function body as shipping: minimise the largest group sum. */
    static int splitArrayLargestSum(int[] nums, int m) {
        long lo = Arrays.stream(nums).max().getAsInt();
        long hi = Arrays.stream(nums).asLongStream().sum();
        return (int) minFeasible(lo, hi, cap -> groupsNeeded(nums, cap) <= m);
    }

    /** LeetCode 1552 / Aggressive Cows -- place k items so the MINIMUM gap is as LARGE as possible. */
    static int maxMinDistance(int[] positions, int k) {
        int[] pos = positions.clone();
        Arrays.sort(pos);
        int n = pos.length;
        return (int) maxFeasible(1, (pos[n - 1] - pos[0]) / (k - 1), d -> {
            int count = 1;
            long last = pos[0];
            for (int i = 1; i < n; i++) {
                if (pos[i] - last >= d) { count++; last = pos[i]; }
            }
            return count >= k;
        });
    }

    /** LeetCode 1482 -- earliest day with m bouquets of k adjacent bloomed flowers, or -1. */
    static int minDaysBouquets(int[] bloomDay, int m, int k) {
        if ((long) m * k > bloomDay.length) return -1;
        long lo = Arrays.stream(bloomDay).min().getAsInt();
        long hi = Arrays.stream(bloomDay).max().getAsInt();
        return (int) minFeasible(lo, hi, day -> {
            int bouquets = 0, run = 0;
            for (int b : bloomDay) {
                if (b <= day) {
                    if (++run == k) { bouquets++; run = 0; }
                } else {
                    run = 0;
                }
            }
            return bouquets >= m;
        });
    }

    /** Real-valued answer: longest L such that the ropes yield at least k pieces of length L. */
    static double maxPieceLength(double[] ropes, int k) {
        double lo = 0.0, hi = Arrays.stream(ropes).max().getAsDouble();
        for (int i = 0; i < 100; i++) {               // fixed halvings, not an epsilon test
            double mid = (lo + hi) / 2;
            double pieces = 0;
            for (double r : ropes) pieces += Math.floor(r / mid);
            if (pieces >= k) lo = mid;
            else hi = mid;
        }
        return lo;
    }

    /** Same answer as maxFeasible using only the lower-midpoint loop: (first INfeasible x) - 1. */
    static long maxFeasibleViaNegation(long lo, long hi, LongPredicate feasible) {
        return minFeasible(lo + 1, hi + 1, x -> !feasible.test(x)) - 1;
    }

    /** LeetCode 378 -- rows and columns ascending. Binary search over VALUES; feasibility = "at least k elements <= x". */
    static int kthSmallestMatrix(int[][] matrix, int k) {
        int n = matrix.length;
        return (int) minFeasible(matrix[0][0], matrix[n - 1][n - 1], x -> {
            int count = 0, col = n - 1;               // staircase walk, O(n): the column pointer only moves left
            for (int row = 0; row < n; row++) {
                while (col >= 0 && matrix[row][col] > x) col--;
                count += col + 1;
            }
            return count >= k;
        });
    }

    public static void main(String[] args) {
        System.out.println("koko [3,6,7,11] h=8:         " + kokoEatingBananas(new int[]{3, 6, 7, 11}, 8));
        // Expected: 4
        System.out.println("koko [30,11,23,4,20] h=5,6:  " + kokoEatingBananas(new int[]{30, 11, 23, 4, 20}, 5) + " " + kokoEatingBananas(new int[]{30, 11, 23, 4, 20}, 6));
        // Expected: 30 23
        System.out.println("ship 1..10 in 5 days:        " + shipWithinDays(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, 5));
        // Expected: 15
        System.out.println("ship [3,2,2,4,1,4] in 3:     " + shipWithinDays(new int[]{3, 2, 2, 4, 1, 4}, 3));
        // Expected: 6
        System.out.println("split [7,2,5,10,8] m=2:      " + splitArrayLargestSum(new int[]{7, 2, 5, 10, 8}, 2));
        // Expected: 18
        System.out.println("split [1,2,3,4,5] m=2:       " + splitArrayLargestSum(new int[]{1, 2, 3, 4, 5}, 2));
        // Expected: 9
        System.out.println("max-min distance k=3:        " + maxMinDistance(new int[]{1, 2, 3, 4, 7}, 3));
        // Expected: 3
        System.out.println("max-min distance k=2:        " + maxMinDistance(new int[]{5, 4, 3, 2, 1, 1000000000}, 2));
        // Expected: 999999999
        System.out.println("bouquets m=3 k=1, k=2:       " + minDaysBouquets(new int[]{1, 10, 3, 10, 2}, 3, 1) + " " + minDaysBouquets(new int[]{1, 10, 3, 10, 2}, 3, 2));
        // Expected: 3 -1
        System.out.println("bouquets m=2 k=3:            " + minDaysBouquets(new int[]{7, 7, 7, 7, 12, 7, 7}, 2, 3));
        // Expected: 12
        System.out.println("rope pieces (k=11):          " + String.format("%.3f", maxPieceLength(new double[]{8.02, 7.43, 4.57, 5.39}, 11)));
        // Expected: 2.005
        System.out.println("kth smallest matrix k=8:     " + kthSmallestMatrix(new int[][]{{1, 5, 9}, {10, 11, 13}, {12, 13, 15}}, 8));
        // Expected: 13
        System.out.println("kth smallest [[-5]] k=1:     " + kthSmallestMatrix(new int[][]{{-5}}, 1));
        // Expected: -5
        System.out.println("negation == upper-mid:       " + maxFeasibleViaNegation(1, 3, d -> d <= 3) + " " + maxFeasible(1, 3, d -> d <= 3));
        // Expected: 3 3

        int[] checks = {0};
        long answer = minFeasible(1, 1_000_000_000L, k -> {
            checks[0]++;
            long hours = 0;
            for (long p : new long[]{1_000_000_000L, 999_999_937L, 500_000_000L}) hours += (p + k - 1) / k;
            return hours <= 10;
        });
        System.out.println("search space 1e9 -> answer, checks: " + answer + " " + checks[0]);
        // Expected: a few dozen checks, not a billion

        Random rng = new Random(11);
        for (int trial = 0; trial < 1500; trial++) {
            int[] piles = new int[1 + rng.nextInt(6)];
            for (int i = 0; i < piles.length; i++) piles[i] = 1 + rng.nextInt(30);
            int h = piles.length + rng.nextInt(26);
            int best = 1;
            while (true) {
                long hours = 0;
                for (int p : piles) hours += (p + best - 1) / best;
                if (hours <= h) break;
                best++;
            }
            if (kokoEatingBananas(piles, h) != best) throw new AssertionError("koko");

            int[] w = new int[1 + rng.nextInt(8)];
            for (int i = 0; i < w.length; i++) w[i] = 1 + rng.nextInt(20);
            int d = 1 + rng.nextInt(w.length);
            int cap = Arrays.stream(w).max().getAsInt();
            while (groupsNeeded(w, cap) > d) cap++;
            if (shipWithinDays(w, d) != cap || splitArrayLargestSum(w, d) != cap) throw new AssertionError("ship");

            int count = 2 + rng.nextInt(7);
            TreeSet<Integer> set = new TreeSet<>();
            while (set.size() < count) set.add(rng.nextInt(40));
            int[] pts = set.stream().mapToInt(Integer::intValue).toArray();
            int kk = 2 + rng.nextInt(count - 1);
            int bestGap = 0;
            for (int mask = 0; mask < (1 << count); mask++) {
                if (Integer.bitCount(mask) != kk) continue;
                int prev = -1, gap = Integer.MAX_VALUE;
                for (int i = 0; i < count; i++) {
                    if ((mask >> i & 1) == 0) continue;
                    if (prev >= 0) gap = Math.min(gap, pts[i] - prev);
                    prev = pts[i];
                }
                bestGap = Math.max(bestGap, gap);
            }
            if (maxMinDistance(pts, kk) != bestGap) throw new AssertionError("distance");

            int g = 1 + rng.nextInt(5);
            int[][] grid = new int[g][g];
            for (int r = 0; r < g; r++)
                for (int c = 0; c < g; c++)
                    grid[r][c] = rng.nextInt(21) + (r > 0 ? grid[r - 1][c] : 0) + (c > 0 ? grid[r][c - 1] : 0);
            int kth = 1 + rng.nextInt(g * g);
            int[] flat = Arrays.stream(grid).flatMapToInt(Arrays::stream).sorted().toArray();
            if (kthSmallestMatrix(grid, kth) != flat[kth - 1]) throw new AssertionError("matrix");

            long cut = 1 + rng.nextInt(12);
            if (maxFeasibleViaNegation(1, 40, x -> x <= cut) != cut || maxFeasible(1, 40, x -> x <= cut) != cut)
                throw new AssertionError("negation");
        }
        System.out.println("randomized check passed: 1500 cases");
    }
}