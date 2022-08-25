use rayon::iter::*;
use rayon::prelude::ParallelSliceMut;
use std::cmp::min;

#[allow(dead_code)]
pub fn par_closest_distance(points: &[(i32, i32)]) -> i64 {
    let mut sorted_points: Vec<(i32, i32)> = points.par_iter().map(|&x| x).collect();
    sorted_points.par_sort_unstable_by_key(|&x| x.0);

    let (min_dist, _) = helper(&sorted_points);
    let sqrt_dist = (min_dist as f64).sqrt();
    sqrt_dist as i64
}

fn helper(points: &[(i32, i32)]) -> (i64, Vec<(i32, i32)>) {
    let length = points.len();
    if length <= 3 {
        let mut smallest = i64::MAX;

        (0..length).into_iter().for_each(|i| {
            (i + 1..length)
                .into_iter()
                .for_each(|x| smallest = min(smallest, dist(points[i], points[x])))
        });

        let mut sort = points.to_vec();
        sort.sort_by_key(|n| n.1);

        return (smallest, sort);
    }

    let half = length / 2;
    let (left, right) = points.split_at(half);
    let ((l, mut_l), (r, mut_r)) = rayon::join(|| helper(left), || helper(right));

    let mut sorted = Vec::with_capacity(points.len());
    unsafe {
        sorted.set_len(points.len());
    }

    sorted = [mut_l, mut_r].concat();

    let mini = min(l, r);

    let idk: Vec<(i32, i32)> = sorted
        .par_iter()
        .filter(|(x, _y)| ((points[half].0 - x).abs() as i64) < mini)
        .map(|i| *i)
        .collect();

    let idk2 = (0..idk.len())
        .into_par_iter()
        .map(|i| {
            let bound = if i + 7 > idk.len() { idk.len() } else { i + 7 };
            (i + 1..bound)
                .into_par_iter()
                .map(|k| dist(idk[k], idk[i]))
                .min()
                .unwrap_or(i64::MAX)
        })
        .reduce(|| mini, |m, x| i64::min(m, x));

    return (i64::min(mini, idk2), sorted);
}

fn dist(a: (i32, i32), b: (i32, i32)) -> i64 {
    (a.0 as i64 - b.0 as i64).pow(2) + (a.0 as i64 - b.0 as i64).pow(2)
}

#[cfg(test)]
mod tests {
    use crate::cp::par_closest_distance;

    #[test]
    fn par_closest_distance_test() {
        assert_eq!(
            1,
            par_closest_distance(&[(12, 5), (15, 2), (0, 0), (1, 8), (5, 3)])
        );
        assert_eq!(
            7,
            par_closest_distance(&[(101, 283423), (732, 103), (96, 37483)])
        );
    }
}
