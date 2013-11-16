function houghStuff(I)
    BW = edge(I, 'canny');
    [H, theta, rho] = hough(BW, 'Theta', -90:0.2:(90-0.2));
    P = houghpeaks(H, 5);
    figure(1), imshow(H)
    lines = houghlines(BW, theta, rho, P);
    figure(2), imshow(BW), hold on
    for k = 1:length(lines)
        xy = [lines(k).point1; lines(k).point2];
        plot(xy(:,1), xy(:,2), 'LineWidth', 2, 'Color', 'green');
    end
end