from pathlib import Path
from PIL import Image, ImageDraw, ImageFont, ImageFilter
import math

ROOT = Path(__file__).parent
OUT = ROOT
W, H = 1080, 1920
FONT_REG = r"C:\Windows\Fonts\segoeui.ttf"
FONT_BOLD = r"C:\Windows\Fonts\segoeuib.ttf"

SCENES = [
    ("source-home.png", "Seu estudo,\nno rumo certo.", "Uma rotina clara para avançar um dia de cada vez.", "COMECE COM UM PLANO"),
    ("source-plan.png", "Um plano que\ncabe na sua vida.", "Organize a semana e acompanhe o que já avançou.", "PLANEJE • EXECUTE • AJUSTE"),
    ("source-edital.png", "Seu edital,\nmais organizado.", "Matérias, tópicos e progresso em um só lugar.", "TUDO NO SEU LUGAR"),
    ("source-ai.png", "IA como apoio.\nVocê no controle.", "Prepare um pedido e use a IA que você preferir.", "IA NO SEU FLUXO"),
    ("source-train.png", "Pratique com\nintenção.", "Escolha o foco e transforme cada sessão em progresso.", "TREINE NO SEU RITMO"),
    ("source-profile.png", "Cada dia\nconta.", "Veja sua constância crescer e celebre cada conquista.", "SEU PROGRESSO, VISÍVEL"),
]

def font(path, size):
    return ImageFont.truetype(path, size)

def gradient_background(index):
    im = Image.new("RGB", (W, H))
    pix = im.load()
    top = (13, 16, 48)
    bottom = (35, 25, 111)
    for y in range(H):
        t = y / (H - 1)
        # Same base at every edge lets the five panels read as one continuous set.
        col = tuple(round(top[i] * (1 - t) + bottom[i] * t) for i in range(3))
        for x in range(W):
            pix[x, y] = col
    glow = Image.new("RGBA", (W, H), (0, 0, 0, 0))
    gd = ImageDraw.Draw(glow)
    centers = [(850, 650), (160, 900), (930, 980), (130, 700), (780, 600), (170, 850)]
    cx, cy = centers[index]
    gd.ellipse((cx-420, cy-420, cx+420, cy+420), fill=(84, 62, 246, 105))
    gd.ellipse((cx-250, cy-250, cx+250, cy+250), fill=(49, 92, 245, 48))
    glow = glow.filter(ImageFilter.GaussianBlur(150))
    return Image.alpha_composite(im.convert("RGBA"), glow)

def curve_points(p0, p1, p2, p3, count=80):
    pts = []
    for i in range(count + 1):
        t = i / count
        q = 1-t
        x = q*q*q*p0[0] + 3*q*q*t*p1[0] + 3*q*t*t*p2[0] + t*t*t*p3[0]
        y = q*q*q*p0[1] + 3*q*q*t*p1[1] + 3*q*t*t*p2[1] + t*t*t*p3[1]
        pts.append((int(x), int(y)))
    return pts

def round_mask(size, radius):
    mask = Image.new("L", size, 0)
    ImageDraw.Draw(mask).rounded_rectangle((0, 0, size[0]-1, size[1]-1), radius, fill=255)
    return mask

def screen_rebrand(source):
    # The live build has not yet been renamed. Replace only the old wordmark in the
    # captured home header, preserving the real screen and all its study data.
    im = source.copy().convert("RGBA")
    patch = Image.new("RGBA", (885, 130))
    pp = patch.load()
    stops = [(0, (70,61,211)), (355, (59,51,187)), (705, (34,39,130)), (884, (15,30,87))]
    for x in range(885):
        for y in range(130):
            for k in range(len(stops)-1):
                if stops[k][0] <= x <= stops[k+1][0]:
                    a, ca = stops[k]
                    b, cb = stops[k+1]
                    t = (x-a)/(b-a)
                    break
            ty = y / 129
            base = tuple(round((ca[i]*(1-t)+cb[i]*t)*(1-0.025*ty)) for i in range(3))
            pp[x, y] = (*base, 255)
    im.alpha_composite(patch, (195, 158))
    d = ImageDraw.Draw(im)
    d.text((211, 164), "Estudário", font=font(FONT_BOLD, 57), fill=(255, 255, 255, 255), stroke_width=0)
    d.text((212, 244), "Seu espaço para estudar", font=font(FONT_REG, 30), fill=(198, 195, 242, 255))
    return im

def phone_layer(screen, angle):
    sw, sh = 632, 1124
    screen = screen.convert("RGB").resize((sw, sh), Image.Resampling.LANCZOS)
    pad = 14
    fw, fh = sw + 2*pad, sh + 2*pad
    frame = Image.new("RGBA", (fw, fh), (11, 12, 27, 255))
    frame.paste(screen.convert("RGBA"), (pad, pad), round_mask((sw, sh), 38))
    fd = ImageDraw.Draw(frame)
    fd.rounded_rectangle((1, 1, fw-2, fh-2), radius=58, outline=(221, 220, 255, 210), width=3)
    # Small earpiece reinforces the realistic device frame without obscuring the app.
    fd.rounded_rectangle((fw//2-55, 7, fw//2+55, 15), radius=5, fill=(22, 22, 36, 255))
    frame = frame.rotate(angle, resample=Image.Resampling.BICUBIC, expand=True)
    shadow = Image.new("RGBA", frame.size, (0, 0, 0, 0))
    shadow.alpha_composite(frame)
    return frame, shadow.filter(ImageFilter.GaussianBlur(25))

def draw_brand(draw, index):
    x, y = 74, 70
    draw.rounded_rectangle((x, y, x+56, y+56), radius=17, fill=(102, 85, 255, 255))
    # Open-book mark
    draw.line([(x+12,y+20),(x+27,y+24),(x+27,y+43),(x+12,y+39),(x+12,y+20)], fill=(255,255,255,255), width=3)
    draw.line([(x+44,y+20),(x+29,y+24),(x+29,y+43),(x+44,y+39),(x+44,y+20)], fill=(255,255,255,255), width=3)
    draw.text((146, 70), "estudário", font=font(FONT_BOLD, 37), fill=(255,255,255,255))
    draw.text((W-170, 82), f"{index+1:02d} / 06", font=font(FONT_BOLD, 23), fill=(190,185,255,255))

def draw_book_mark(draw, cx, cy, size, color, crease=(76,67,232,255)):
    s = size / 100
    def pts(values):
        return [(int(cx + x*s), int(cy + y*s)) for x, y in values]
    draw.polygon(pts([(-42,-31),(-4,-23),(-4,34),(-42,25)]), fill=color)
    draw.polygon(pts([(4,-23),(42,-31),(42,25),(4,34)]), fill=color)
    draw.line(pts([(0,-23),(0,34)]), fill=crease, width=max(3, int(5*s)))

def draw_launcher_glyph(draw, left, top, size):
    # Mirror ic_launcher_foreground.xml so the Play listing and installed app use the same mark.
    scale = size / 108
    def point(x, y):
        x = 54 + (x - 54) * .66
        y = 59.5 + (y - 59.5) * .66 - 5.5
        return (left + x * scale, top + y * scale)
    def polygon(points, color):
        draw.polygon([point(x, y) for x, y in points], fill=color)
    def stroke(points, width, color):
        mapped = [point(x, y) for x, y in points]
        px = max(1, round(width * .66 * scale))
        radius = px / 2
        draw.line(mapped, fill=color, width=px, joint="curve")
        for x, y in (mapped[0], mapped[-1]):
            draw.ellipse((x-radius, y-radius, x+radius, y+radius), fill=color)

    white = (255, 255, 255, 255)
    ink = (51, 38, 206, 255)
    mint = (126, 224, 184, 255)
    polygon([(16,28),(51,34),(51,82),(16,76)], white)
    polygon([(57,34),(92,28),(92,76),(57,82)], white)
    stroke([(22,43.53),(38,46.27)], 3.6, ink)
    stroke([(22,52.53),(45,56.47)], 2.6, ink)
    stroke([(22,60.03),(45,63.97)], 2.6, ink)
    stroke([(22,67.53),(36,69.93)], 2.6, ink)
    stroke([(67.5,53.5),(73,59),(84.5,45)], 3.8, ink)
    stroke([(66,72.96),(86,69.53)], 2.6, ink)
    polygon([(60,32.89),(64.5,32.11),(64.5,91),(62.25,88),(60,91)], mint)

def make_store_icon():
    scale = 4
    size = 512
    im = Image.new("RGBA", (size*scale, size*scale), (51,38,206,255))
    draw_launcher_glyph(ImageDraw.Draw(im), 0, 0, size*scale)
    im.resize((size,size), Image.Resampling.LANCZOS).convert("RGB").save(
        ROOT / "estudario-play-store-icon-512.png", optimize=True)

    fg_size = 432
    fg = Image.new("RGBA", (fg_size*scale, fg_size*scale), (0,0,0,0))
    draw_launcher_glyph(ImageDraw.Draw(fg), 0, 0, fg_size*scale)
    fg.resize((fg_size,fg_size), Image.Resampling.LANCZOS).save(
        ROOT / "estudario-adaptive-foreground-432.png", optimize=True)

def make_feature_graphic():
    fw, fh = 1024, 500
    im = Image.new("RGB", (fw,fh))
    p = im.load()
    for y in range(fh):
        t = y/(fh-1)
        for x in range(fw):
            u = x/(fw-1)
            p[x,y] = (round(13+12*t+9*u), round(17+11*t+7*u), round(50+57*t+30*u))
    glow = Image.new("RGBA", (fw,fh), (0,0,0,0))
    gd = ImageDraw.Draw(glow)
    gd.ellipse((570,-120,1080,390), fill=(89,72,255,125))
    glow = glow.filter(ImageFilter.GaussianBlur(95))
    im = Image.alpha_composite(im.convert("RGBA"), glow)
    d = ImageDraw.Draw(im)
    d.rounded_rectangle((64,58,124,118), radius=18, fill=(102,85,255,255))
    draw_book_mark(d, 94, 88, 46, (255,255,255,255))
    d.text((145,61), "estudário", font=font(FONT_BOLD, 39), fill=(255,255,255,255))
    d.text((66,173), "Seu estudo,\nno rumo certo.", font=font(FONT_BOLD, 67), fill=(255,255,255,255), spacing=0)
    d.text((70,346), "Planeje • Estude • Evolua", font=font(FONT_REG, 27), fill=(213,210,255,255))
    # Show the real current Home screen in a gently tilted device on the right.
    screen = screen_rebrand(Image.open(ROOT / "source-home.png")).convert("RGB")
    sw, sh = 284, 505
    screen = screen.resize((sw,sh), Image.Resampling.LANCZOS)
    pad = 9
    phone = Image.new("RGBA", (sw+pad*2, sh+pad*2), (10,11,28,255))
    phone.paste(screen, (pad,pad), round_mask((sw,sh), 24))
    pd = ImageDraw.Draw(phone)
    pd.rounded_rectangle((1,1,sw+2*pad-2,sh+2*pad-2), radius=38, outline=(223,221,255,220), width=2)
    phone = phone.rotate(-5, resample=Image.Resampling.BICUBIC, expand=True)
    shadow = Image.new("RGBA", phone.size, (0,0,0,0))
    shadow.alpha_composite(phone)
    shadow = shadow.filter(ImageFilter.GaussianBlur(16))
    im.alpha_composite(shadow, (694, 13))
    im.alpha_composite(phone, (680, 0))
    # Continuous fine lilac trail ties this graphic to the screenshot carousel.
    d = ImageDraw.Draw(im)
    d.line([(0,478),(300,478),(610,465),(770,470)], fill=(154,145,255,190), width=3)
    im.convert("RGB").save(ROOT / "estudario-play-store-feature-1024x500.png", optimize=True)

for idx, (filename, title, subtitle, kicker) in enumerate(SCENES):
    bg = gradient_background(idx)
    d = ImageDraw.Draw(bg)
    draw_brand(d, idx)
    d.text((76, 192), kicker, font=font(FONT_BOLD, 21), fill=(171,164,255,255), spacing=6)
    d.multiline_text((72, 245), title, font=font(FONT_BOLD, 76), fill=(255,255,255,255), spacing=0, stroke_width=0, align="left")
    d.multiline_text((78, 430), subtitle, font=font(FONT_REG, 31), fill=(211,210,239,255), spacing=10)

    screen = Image.open(ROOT / filename)
    if filename == "source-home.png":
        screen = screen_rebrand(screen)
    phone, shadow = phone_layer(screen, [-4, 4, -3, 4, -4, 3][idx])
    # Alternating placement makes the tilted phones feel like one continuous journey.
    x = 332 if idx % 2 == 0 else 270
    y = 610
    bg.alpha_composite(shadow, (x+4, y+14))
    bg.alpha_composite(phone, (x, y))

    # A luminous progress trail meets at identical points on each panel edge.
    d = ImageDraw.Draw(bg)
    path = curve_points((0, 1785), (290, 1740 if idx % 2 == 0 else 1830), (760, 1830 if idx % 2 == 0 else 1740), (W, 1785))
    d.line(path, fill=(142,132,255,190), width=4)
    for px, py in [path[0], path[20], path[40], path[60], path[-1]]:
        d.ellipse((px-5, py-5, px+5, py+5), fill=(222,219,255,255))
    # Progress pills: the active stage advances from one screenshot to the next.
    pill_y = 1870
    total_w, gap, pill_h = 150, 13, 9
    start = (W - (total_w*len(SCENES) + gap*(len(SCENES)-1))) // 2
    for j in range(len(SCENES)):
        color = (255,255,255,245) if j == idx else (153,146,219,130)
        d.rounded_rectangle((start+j*(total_w+gap), pill_y, start+j*(total_w+gap)+total_w, pill_y+pill_h), radius=6, fill=color)

    out = OUT / f"{idx+1:02d}-estudario-play-store.png"
    bg.convert("RGB").save(out, optimize=True)
    print(out)

# Contact sheet for review in the workspace.
thumb_w, thumb_h = 270, 480
sheet = Image.new("RGB", (thumb_w*len(SCENES), thumb_h), (12, 14, 40))
for i in range(len(SCENES)):
    p = Image.open(OUT / f"{i+1:02d}-estudario-play-store.png").resize((thumb_w, thumb_h), Image.Resampling.LANCZOS)
    sheet.paste(p, (i*thumb_w, 0))
sheet.save(OUT / "estudario-play-store-contact-sheet.png", optimize=True)
make_store_icon()
make_feature_graphic()
