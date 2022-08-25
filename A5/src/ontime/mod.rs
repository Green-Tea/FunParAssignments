use chashmap::CHashMap;
use rayon::iter::*;
use rayon::prelude::*;
use std::{fs, io};

#[derive(Debug, PartialEq)]
struct FlightRecord {
    unique_carrier: String,
    actual_elapsed_time: i32,
    arrival_delay: i32,
}

#[allow(dead_code)]
fn parse_line(line: &str) -> Option<FlightRecord> {
    let line_split = par_split(line, ',');

    let flight = FlightRecord {
        unique_carrier: String::from(line_split[8]),
        actual_elapsed_time: if line_split[11].parse::<i32>().is_ok() {
            line_split[11].parse().unwrap()
        } else {
            0
        },
        arrival_delay: if line_split[14].parse::<i32>().is_ok() {
            line_split[14].parse().unwrap()
        } else {
            0
        },
    };
    Some(flight)
}

#[allow(dead_code)]
pub fn ontime_rank(filename: &str) -> Result<Vec<(String, f64)>, io::Error> {
    let contents = fs::read_to_string(filename).unwrap();
    let slice: &str = &contents;
    let lines: Vec<&str> = slice.lines().collect();

    let ontime = CHashMap::new();
    lines.into_par_iter().for_each(|i| {
        let flag = parse_line(i);
        if flag.is_some() {
            let ans = flag.unwrap();
            if ans.arrival_delay <= 0 {
                ontime.upsert(
                    ans.unique_carrier,
                    || (1, 1),
                    |(s, t)| {
                        *s += 1;
                        *t += 1;
                    },
                )
            } else {
                ontime.upsert(ans.unique_carrier, || (0, 1), |(_, t)| *t += 1)
            }
        }
    });

    ontime.remove(&String::from("UniqueCarrier"));

    let mut proportion: Vec<(String, f64)> = ontime
        .into_iter()
        .map(|(k, (x, y))| (k.clone(), x as f64 / y as f64))
        .collect();
    proportion.par_sort_unstable_by(|(_, n), (_, n1)| n1.partial_cmp(n).unwrap());

    Ok(proportion)
}

fn plus_scan(xs: Vec<i32>) -> (Vec<i32>, i32) {
    if xs.is_empty() {
        return (vec![], 0);
    }
    let half = xs.len() / 2;
    let (c_prefix, mut c_sum) = plus_scan(
        (0..half)
            .into_par_iter()
            .map(|i| xs[2 * i] + xs[2 * i + 1])
            .collect::<Vec<i32>>(),
    );
    let mut pfs: Vec<i32> = (0..half)
        .into_par_iter()
        .flat_map(|i| vec![c_prefix[i], c_prefix[i] + xs[2 * i]])
        .collect();
    if xs.len() % 2 == 1 {
        pfs.push(c_sum);
        c_sum += xs[xs.len() - 1];
    }

    (pfs, c_sum)
}

pub fn par_split(str_buff: &str, split_char: char) -> Vec<&str> {
    let mut res = Vec::new();
    let check = str_buff
        .par_chars()
        .into_par_iter()
        .map(|x| if fcheck(x, split_char) { 1 } else { 0 })
        .collect::<Vec<i32>>();

    let (index, _size) = plus_scan(check);
    let mut k = 0;
    for i in 1..index.len() - 1 {
        if index[i] < index[i + 1] {
            let word = &str_buff[k..i];
            res.push(word.clone());
            k = i + 1;
        }
    }
    res.push(&str_buff[k..index.len()]);
    res
}

fn fcheck(str_buff: char, split_char: char) -> bool {
    if str_buff == split_char {
        true
    } else {
        false
    }
}

#[cfg(test)]
mod tests {
    use crate::ontime::{par_split, parse_line, FlightRecord};
    use std::str::FromStr;

    #[test]
    fn par_split_test() {
        assert_eq!(
            vec!["Lorem", "ipsum", "dolor", " ", "sit", "", "amet"],
            par_split("Lorem,ipsum,dolor, ,sit,,amet", ',')
        );
    }

    #[test]
    fn pars_line_test() {
        let flight_record = FlightRecord {
            unique_carrier: String::from("WN"),
            actual_elapsed_time: i32::from_str("0").unwrap(),
            arrival_delay: i32::from_str("0").unwrap(),
        };

        assert_eq!(Some(flight_record),parse_line("2008,1,3,4,2003,1955,2211,2225,WN,335,N712SW,NA,150,116,NA,8,IAD,TPA,810,4,8,0,,0,NA,NA,NA,NA,NA"));
    }
}
