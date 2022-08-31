import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISlocalite } from '../slocalite.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../slocalite.test-samples';

import { SlocaliteService } from './slocalite.service';

const requireRestSample: ISlocalite = {
  ...sampleWithRequiredData,
};

describe('Slocalite Service', () => {
  let service: SlocaliteService;
  let httpMock: HttpTestingController;
  let expectedResult: ISlocalite | ISlocalite[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SlocaliteService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Slocalite', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const slocalite = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(slocalite).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Slocalite', () => {
      const slocalite = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(slocalite).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Slocalite', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Slocalite', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Slocalite', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSlocaliteToCollectionIfMissing', () => {
      it('should add a Slocalite to an empty array', () => {
        const slocalite: ISlocalite = sampleWithRequiredData;
        expectedResult = service.addSlocaliteToCollectionIfMissing([], slocalite);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(slocalite);
      });

      it('should not add a Slocalite to an array that contains it', () => {
        const slocalite: ISlocalite = sampleWithRequiredData;
        const slocaliteCollection: ISlocalite[] = [
          {
            ...slocalite,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSlocaliteToCollectionIfMissing(slocaliteCollection, slocalite);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Slocalite to an array that doesn't contain it", () => {
        const slocalite: ISlocalite = sampleWithRequiredData;
        const slocaliteCollection: ISlocalite[] = [sampleWithPartialData];
        expectedResult = service.addSlocaliteToCollectionIfMissing(slocaliteCollection, slocalite);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(slocalite);
      });

      it('should add only unique Slocalite to an array', () => {
        const slocaliteArray: ISlocalite[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const slocaliteCollection: ISlocalite[] = [sampleWithRequiredData];
        expectedResult = service.addSlocaliteToCollectionIfMissing(slocaliteCollection, ...slocaliteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const slocalite: ISlocalite = sampleWithRequiredData;
        const slocalite2: ISlocalite = sampleWithPartialData;
        expectedResult = service.addSlocaliteToCollectionIfMissing([], slocalite, slocalite2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(slocalite);
        expect(expectedResult).toContain(slocalite2);
      });

      it('should accept null and undefined values', () => {
        const slocalite: ISlocalite = sampleWithRequiredData;
        expectedResult = service.addSlocaliteToCollectionIfMissing([], null, slocalite, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(slocalite);
      });

      it('should return initial array if no Slocalite is added', () => {
        const slocaliteCollection: ISlocalite[] = [sampleWithRequiredData];
        expectedResult = service.addSlocaliteToCollectionIfMissing(slocaliteCollection, undefined, null);
        expect(expectedResult).toEqual(slocaliteCollection);
      });
    });

    describe('compareSlocalite', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSlocalite(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSlocalite(entity1, entity2);
        const compareResult2 = service.compareSlocalite(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSlocalite(entity1, entity2);
        const compareResult2 = service.compareSlocalite(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSlocalite(entity1, entity2);
        const compareResult2 = service.compareSlocalite(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
