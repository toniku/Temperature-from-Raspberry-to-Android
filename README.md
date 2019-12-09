# Temperature-from-Raspberry-to-Android
School group-project that includes temperature measurement with Raspberry and Android app for displaying the results

The purpose of this project was to create a temperature monitoring application, which utilizes Amazon Web Services.
We developed a system that allowed the tracking of current temperature values through an Android application. This system used different services offered by AWS for data transferring between the hardware and the mobile app.

We used a DS18B20-temperature sensor for temperature measurement. Between intervals, the Python program would send the sensor values to AWS' IoT Core. We would then use internal rules and policies to transfer the data to a Dynamo database, then to the API Gateway, and from there to an HTTP-website. From here, the mobile application was finally (with our skills) able to retrieve the desired data.

Our application ended up working as we intended it to. Undeniably, with more time, additional features and improvements could have been included in our final product. Taking note of the minimal schedule we had during this project, the functionality of the product turned out adequate.

