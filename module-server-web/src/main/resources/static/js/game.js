
$(document).on('click', '[ffw-type="Country"]', function (e) {
        e.preventDefault();
        let self = this;
        try {
            let ffwCountryKey = self.getAttribute("ffw-country-key");
            if (ffwCountryKey == null) {
                throw "The ffw-country-key is not defined!";
            }

            self.style.fill = "pink";

            document.getElementById('SelectedCountry').innerHTML = 'Selected country key:' + ffwCountryKey;
        } catch (exp) {
            console.error(exp)
        }
    }
);

$(document).on('mouseover', '[ffw-type="Country"]', function (e) {
        e.preventDefault();
        let self = this;
        try {
            let ffwCountryKey = self.getAttribute("ffw-country-key");
            if (ffwCountryKey == null) {
                throw "The ffw-country-key is not defined!";
            }
            self.style.backgroundColor = "green";
            document.getElementById('PointedCountry').innerHTML = 'Pointed country key:' + ffwCountryKey;
        } catch (exp) {
            console.error(exp)
        }
    }
);


