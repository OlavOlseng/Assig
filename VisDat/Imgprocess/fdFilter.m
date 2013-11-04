function res = fdFilter(image, filter)
    SFT = fftshift(fft2(image));
    filteredSFT = SFT .* filter;
    res = ifft2(ifftshift(filteredSFT));
end