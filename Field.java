import java.util.ArrayList;
import java.util.Random;

public class Field
{
    ArrayList<Location> locationlist = new ArrayList<Location>();
    int width;
    int height;
    Random random = new Random();

    // construtor da classe
    public Field(int width, int height, float bombChance)
    {
        this.width = width;
        this.height = height;
        createField(this.width, this.height, bombChance);
    }

    /*
    Seleciona/revela a localização
    se for Ground vai ver o numero de bombas ao redor
    se for Bomb vai dar game over
     */
    public void selectLocation(int x, int y)
    {
        Location l = getLocation(x, y);
        if(l != null)
        {
            l.setHiddenFalse();
            if(l instanceof Ground)
            {
                lookAround(((Ground) l));
            }
            else if(l instanceof Bomb)
            {
                System.out.println("Game Over");
            }
        }
    }

    /*
    Obtem e retorna um location de acordo com sua cordenada x e y
    se não existir retorna nulo
     */
    public Location getLocation(int x, int y)
    {
        for (Location l : locationlist) {
            if (l.getX() == x && l.getY() == y)
            {
                return l;
            }
        }
        return null;
    }

    /*
    Recebe um Location, conta o número de bombas ao redor dele e envia o valor para ele
    se o valor for zero ele vai selecionar os Locations ao redor
     */
    public void lookAround(Location g)
    {
        if(g instanceof Ground)
        {
            int x = g.getX();
            int y = g.getY();
            int bombsAround = 0;
            for(int j = y - 1; j < y + 2; j += 1)
            {
                for(int i = x - 1; i < x + 2; i += 1)
                {
                    if(getLocation(i, j) != null)
                    {
                        if(getLocation(i, j) instanceof Bomb)
                        {
                            bombsAround += 1;
                        }
                    }
                }
            }
            ((Ground) g).setBombNumber(bombsAround);
            if(bombsAround == 0)
            {
                selectAround((Ground) g);
            }
        }
    }

    /*
    Seleciona os Locations ao redor
     */
    public void selectAround(Ground g)
    {
        int y = g.getY();
        int x = g.getX();
        for(int j = y - 1; j < y + 2; j += 1)
        {
            for(int i = x - 1; i < x + 2; i += 1)
            {
                if(getLocation(i, j) != null)
                {
                    Location l = getLocation(i, j);
                    if(l instanceof Ground && l.getHidden())
                    {
                        selectLocation(l.getX(), l.getY());
                    }
                }
            }
        }
    }

    /*
    Pega um Location
    se não for nulo, retorna suas informaçõe
    caso contrario, printa "fora dos limites"
     */
    public void printLocationInfo(int x, int y)
    {
        if (getLocation(x, y) != null)
        {
            getLocation(x, y).printInfo();
        }
        else
        {
            System.out.println("fora dos limites");
        }
    }

    //Printa a representação
    public String printLocationRepresentation(int x, int y)
    {
        if (getLocation(x, y) != null)
        {
            return getLocation(x, y).printRepresentation();
        }
        else
        {
            return "";
        }
    }

    // printa o campo inteiro
    public void printField()
    {
        System.out.print("    ");
        for(int c = 1; c <= this.width; c += 1)
        {
            System.out.print(c + " ");
        }
        System.out.println();
        for(int j = 1; j <= this.height; j += 1)
        {
            System.out.print(j + " ( ");
            for(int i = 1; i <= this.width; i += 1)
            {
                System.out.print(printLocationRepresentation(i, j));
            }
            System.out.println(")");
        }
    }

    // Recebe um Location e adiciona na lista de Locations
    public void addLocation(Location l)
    {
        locationlist.add(l);
    }

    // Cria o campo minado
    public void createField(int width, int height,float bombChance)
    {
        double chance;
        if(bombChance >= 100)
        {
            chance = 1;
        }
        else if(bombChance < 100 && bombChance > 0)
        {
            chance = bombChance/100;
        }
        else
        {
            chance = 0.01;
        }
        for(int j = 1; j <= height; j += 1)
        {
            for(int i = 1; i <= width; i += 1)
            {
                double isBomb = random.nextDouble();
                if(isBomb <= chance)
                {
                    addLocation(new Bomb(i, j, "bomb"));
                }
                else
                {
                    addLocation(new Ground(i, j, "not bomb"));
                }
            }
        }
    }
}
