# CatalogAssignment
Konda Chaithanya Krishna  (Shamir's Secret Sharing algorithm.)
# Polynomial Secret Decoder

This project implements a solution to decode a polynomial secret from a set of points. The points are provided in different base formats, which are decoded and used to perform Lagrange interpolation to find the secret (constant term) of the polynomial.

## Project Description

- The input data is provided in a JSON format with different bases for each value.
- The program decodes the values based on the provided base and uses the Lagrange interpolation method to compute the secret constant term of the polynomial.
- The decoded points are processed, and the secret value is computed and printed.

## How It Works

1. **Decode Values**: The program supports decoding of values in different bases (e.g., binary, hexadecimal, octal).
2. **Lagrange Interpolation**: The program uses Lagrange interpolation to compute the secret based on the decoded points.
3. **JSON Input**: The input data is read from a `test_case.json` file.

## Input Format

The `test_case.json` file should contain data in the following format:
```json
{
"keys": {
    "n": 10,
    "k": 7
  },
  "1": {
    "base": "6",
    "value": "13444211440455345511"
  },
  "2": {
    "base": "15",
    "value": "aed7015a346d63"
  },
  "3": {
    "base": "15",
    "value": "6aeeb69631c227c"
  },
  "4": {
    "base": "16",
    "value": "e1b5e05623d881f"
  },
  "5": {
    "base": "8",
    "value": "316034514573652620673"
  },
  "6": {
    "base": "3",
    "value": "2122212201122002221120200210011020220200"
  },
  "7": {
    "base": "3",
    "value": "20120221122211000100210021102001201112121"
  },
  "8": {
    "base": "6",
    "value": "20220554335330240002224253"
  },
  "9": {
    "base": "12",
    "value": "45153788322a1255483"
  },
  "10": {
    "base": "7",
    "value": "1101613130313526312514143"
  }
}