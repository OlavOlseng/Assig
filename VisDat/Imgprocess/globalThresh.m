function res = globalThresh(image)
    done = false;
    T = mean2(image);
    while (~done)
        temp = image>T;
        Tnext = (mean2(image(temp)) + mean2(image(~temp)))/2;
        done = abs(T-Tnext) < 0.5;
        T = Tnext;
    end
    res = thresh(image, T);
end